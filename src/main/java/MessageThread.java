/**
 * @author Jayden Chan
 * @version 1.0
 * Date Created: March 2, 2018
 *
 * Parses a given conversation thread.
 */

import java.util.*;
import java.util.regex.*;

import java.io.File;
import java.io.FileNotFoundException;

public class MessageThread {

    private String threadString;
    private String title;
    private String user;
    private ArrayList<String> participants;
    private ArrayList<String> messages;
    private Map<String, Integer> messageMap = new HashMap<>();

    private int totalMessages;
    private int wordCount;
    private int charCount;
    private double wordsPerMessage;
    private double charsPerMessage;

    public MessageThread(String fileName) {
        String threadStringTemp = parseThreadToString(fileName);
        threadString            = threadStringTemp.replaceAll("&#039;", "'");
        threadString            = threadStringTemp.replaceAll("&quot;", "\"");

        title                   = parseTitle();
        participants            = parseParticipants();
        user                    = parseUser();
        initMessageMap();
        messages                = parseMessages();
        totalMessages           = messages.size();
        computeWordAndCharCount();
        wordsPerMessage         = (double)wordCount / (double)totalMessages;
        charsPerMessage         = (double)charCount / (double)totalMessages;
    }

/****************************************************************/
/*                        Getter methods                        */
/****************************************************************/

    public String getTitle() {
        return title;
    }

    public String getUser() {
        return user;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public Map<String, Integer> getMessageMap() {
        return messageMap;
    }

    public int getTotalMessages() {
        return totalMessages;
    }

    public int getWordCount() {
        return wordCount;
    }

    public double getWordsPerMessage() {
        return wordsPerMessage;
    }

    public double getCharsPerMessage() {
        return charsPerMessage;
    }

/****************************************************************/
/*                    Private helper methods                    */
/****************************************************************/

    private void computeWordAndCharCount() {
        for(String s : messages) {
            wordCount += wordCount(s);
            charCount += s.length();
        }
    }

    private int wordCount(String str) {
        return str.split("\\s+").length;
    }

    private ArrayList<String> parseMessages() {
        ArrayList<String> toReturn = new ArrayList<>();

        Pattern messagePattern = Pattern.compile("(?<=<p>)(.*?)(?=<)");
        Matcher messageMatcher = messagePattern.matcher(threadString);

        Pattern senderPattern = Pattern.compile("(?<=\"user\">)(.*?)(?=<)");
        Matcher senderMatcher = senderPattern.matcher(threadString);

        while(messageMatcher.find()) {
            toReturn.add(messageMatcher.group());

            if(senderMatcher.find()) {
                addMessageToUser(senderMatcher.group());
            }
        }

        return toReturn;
    }

    private void addMessageToUser(String user) {
        for(String s : participants) {
            if(s.equals(user)) {
                int prev = messageMap.get(s);
                messageMap.replace(s, prev + 1);
                return;
            }
        }

        if(user.equals(this.user)) {
            int prev = messageMap.get(user);
            messageMap.replace(user, prev + 1);
        }
    }

    private void initMessageMap() {
        for(String s : participants) {
            messageMap.put(s, 0);
        }

        messageMap.put(user, 0);
    }

    private String parseThreadToString(String file) throws IllegalArgumentException {
        Scanner sc;
        String line = "";

        // Initialize the scanner with the file we need
        try {
            sc = new Scanner(new File(file));
        }
        catch(FileNotFoundException e) {
            throw new IllegalArgumentException("ERROR: File \"" + file + "\" not found");
        }

        // Get the line with the conversation in it
        while(sc.hasNextLine()) {
            String temp = sc.nextLine();
            if(temp.startsWith("</style><title>Conversation with")) {
                StringBuilder sb = new StringBuilder();
                sb.append(temp);
                while(sc.hasNextLine()) {
                    sb.append(sc.nextLine());
                }

                line = sb.toString();
            }
        }

        // Throw error if the file did not contain a conversation
        if(line.equals("")) {
            throw new IllegalArgumentException("ERROR: The file provided does not contain a Facebook conversation.");
        }

        return line;
    }

    private String parseTitle() {
        Pattern titlePattern = Pattern.compile("(?<=<title>)(.*?)(?=<)");
        Matcher titleMatcher = titlePattern.matcher(threadString);

        if(titleMatcher.find()) {
            return titleMatcher.group();
        }
        else {
            return null;
        }
    }

    private ArrayList<String> parseParticipants() {
        ArrayList<String> toReturn = new ArrayList<>();

        Pattern participantsPattern = Pattern.compile("(?<=Participants: )(.*?)(?=<)");
        Matcher participantsMatcher = participantsPattern.matcher(threadString);

        if(participantsMatcher.find()) {
            String result = participantsMatcher.group();
            toReturn = new ArrayList<String>(Arrays.asList(result.split(",")));
        }

        for(int i = 0; i < toReturn.size(); i++) {
            String current = toReturn.get(i);
            toReturn.remove(i);
            toReturn.add(i, current.trim());
        }

        return toReturn;
    }

    private String parseUser() {
        Pattern senderPattern = Pattern.compile("(?<=\"user\">)(.*?)(?=<)");
        Matcher senderMatcher = senderPattern.matcher(threadString);

        while(senderMatcher.find()) {
            String current = senderMatcher.group();
            if(!participants.contains(current)) {
                return current;
            }
        }

        return null;
    }

/****************************************************************/
/*                           Override                           */
/****************************************************************/

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Title: ");
        sb.append(title+"\n");
        sb.append("Self: " + user + "\n");
        sb.append("Participants: ");

        for(String p : participants) {
            sb.append(p);
            sb.append(", ");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);

        sb.append("\n\nMessage breakdown:\n");

        for(String s : participants) {
            int currMessages = messageMap.get(s);
            sb.append(s + ": ");
            sb.append(currMessages);
            sb.append(String.format(" (%-3.1f%%)", ((double)currMessages / (double)totalMessages) * 100));
            sb.append("\n");
        }

        sb.append(user + ": ");
        sb.append(messageMap.get(user));
        sb.append(String.format(" (%-3.1f%%)\n\n", ((double)messageMap.get(user) / (double)totalMessages) * 100));
        sb.append("Total messages:    " + totalMessages + "\n");
        sb.append("Total words:       " + wordCount + "\n");
        sb.append("Words per message: " + String.format("%-1.2f", wordsPerMessage) + "\n");
        sb.append("Chars per message: " + String.format("%-1.2f", charsPerMessage) + "\n");

        return sb.toString();
    }
}
