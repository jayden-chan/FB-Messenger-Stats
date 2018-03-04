/**
 * @author Jayden Chan
 * @version 1.0
 * Date Created: March 3, 2018
 *
 * Generates fancy-looking HTML files with graphs and charts.
 */

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;
import java.nio.file.*;
import java.nio.charset.*;
import static java.nio.file.StandardCopyOption.*;
import java.util.*;
import java.text.DecimalFormat;

public class Writer {

    public static void genSingle(MessageThread thread, String fileName) {
        ArrayList<String> participants = thread.getParticipants();
        String partner = participants.get(0);
        Map<String, Integer> messageMap = thread.getMessageMap();

        DecimalFormat df = new DecimalFormat("###,###");

        try {
            // Copy the template file to memory
            String toWrite = fileToString("assets/SingleThreadTemplate.html", StandardCharsets.UTF_8);

            toWrite = toWrite.replaceAll("TITLE", thread.getTitle());
            toWrite = toWrite.replaceAll("MESS" , df.format(thread.getTotalMessages()));
            toWrite = toWrite.replaceAll("DAYS" , String.valueOf(thread.getDays()));
            toWrite = toWrite.replaceAll("WRDS" , df.format(thread.getWordCount()));
            toWrite = toWrite.replaceAll("WSPM" , String.format("%-2.2f", thread.getWordsPerMessage()));
            toWrite = toWrite.replaceAll("CHPM" , String.format("%-2.2f", thread.getCharsPerMessage()));
            toWrite = toWrite.replaceAll("MSPD" , String.format("%-2.2f", thread.getMessagesPerDay()));

            StringBuilder sb = new StringBuilder(toWrite);
            String user1ToAdd = "['" + thread.getUser() + "', " + String.valueOf(messageMap.get(thread.getUser())) + "],\n";
            String user2ToAdd = "['" + partner + "', " + String.valueOf(messageMap.get(partner)) + "],\n";
            sb.insert(357, user1ToAdd);
            sb.insert(357 + user1ToAdd.length(), user2ToAdd);
            toWrite = sb.toString();

            FileWriter pw = new FileWriter(new File("out/"+fileName));
            pw.write(toWrite);
            pw.flush();
            pw.close();

            System.out.println("Generated HTML file \"" + fileName +"\" successfully.");
        }
        catch(IOException e) {
            System.out.println("HTML file generation unsuccessful.");
            System.out.println(e);
        }
    }

    public static void genGroup(MessageThread thread, String fileName) {
        ArrayList<String> participants = thread.getParticipants();
        Map<String, Integer> messageMap = thread.getMessageMap();

        DecimalFormat df = new DecimalFormat("###,###");

        String title = thread.getTitle();
        title = title.substring(18);

        try {
            // Copy the template file to memory
            String toWrite = fileToString("assets/SingleThreadTemplate.html", StandardCharsets.UTF_8);

            toWrite = toWrite.replaceAll("TITLE", title);
            toWrite = toWrite.replaceAll("MESS" , df.format(thread.getTotalMessages()));
            toWrite = toWrite.replaceAll("DAYS" , String.valueOf(thread.getDays()));
            toWrite = toWrite.replaceAll("WRDS" , df.format(thread.getWordCount()));
            toWrite = toWrite.replaceAll("WSPM" , String.format("%-2.2f", thread.getWordsPerMessage()));
            toWrite = toWrite.replaceAll("CHPM" , String.format("%-2.2f", thread.getCharsPerMessage()));
            toWrite = toWrite.replaceAll("MSPD" , String.format("%-2.2f", thread.getMessagesPerDay()));

            StringBuilder sb = new StringBuilder(toWrite);

            int charToWrite = 357;
            for(String s : participants) {
                String toAdd = "['" + s + "', " + messageMap.get(s) + "],\n";
                sb.insert(charToWrite, toAdd);
                charToWrite += toAdd.length();
            }

            String toAdd = "['" + thread.getUser() + "', " + messageMap.get(thread.getUser()) + "],\n";
            sb.insert(charToWrite, toAdd);

            toWrite = sb.toString();

            FileWriter pw = new FileWriter(new File("out/"+fileName));
            pw.write(toWrite);
            pw.flush();
            pw.close();

            System.out.println("Generated HTML file \"" + fileName +"\" successfully.");
        }
        catch(IOException e) {
            System.out.println("HTML file generation unsuccessful.");
            System.out.println(e);
        }

    }

    private static String fileToString(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
