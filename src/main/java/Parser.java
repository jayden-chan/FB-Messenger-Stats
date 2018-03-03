/**
 * @author Jayden Chan
 * @version 1.0
 * Date Created: March 2, 2018
 *
 * Parses a given conversation thread.
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.*;

import java.io.File;
import java.io.FileNotFoundException;

public class Parser {

    private String threadString;
    private String title;
    private ArrayList<String> participants;

    public Parser(String fileName) {
        threadString = parseThreadToString(fileName).replaceAll("&#039;", "'");
        title = parseTitle();
        participants = parseParticipants();
    }

    /****************************************************************/
    /*                    Private helper methods                    */
    /****************************************************************/

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

        return toReturn;
    }

    /****************************************************************/
    /*                           Override                           */
    /****************************************************************/

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Title: ");
        sb.append(title+"\n");
        sb.append("Participants: ");

        for(String p : participants) {
            sb.append(p);
            sb.append(",");
        }

        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }
}