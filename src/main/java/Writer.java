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

public class Writer {

    public static void genSingle(MessageThread thread, String fileName) {
        ArrayList<String> participants = thread.getParticipants();
        String partner = participants.get(0);
        Map<String, Integer> messageMap = thread.getMessageMap();

        int byUser = messageMap.get(thread.getUser());
        int byPartner = messageMap.get(partner);

        try {
            // Copy the template file to memory
            String toWrite = fileToString("assets/SingleThreadTemplate.html", StandardCharsets.UTF_8);

            toWrite = toWrite.replaceAll("TITLE", thread.getTitle());
            toWrite = toWrite.replaceAll("USR1" , thread.getUser());
            toWrite = toWrite.replaceAll("USR2" , partner);
            toWrite = toWrite.replaceAll("DAYS" , "test");
            toWrite = toWrite.replaceAll("MESS" , String.valueOf(thread.getTotalMessages()));
            toWrite = toWrite.replaceAll("123"  , String.valueOf(byUser));
            toWrite = toWrite.replaceAll("234"  , String.valueOf(byPartner));
            toWrite = toWrite.replaceAll("WRDS" , String.valueOf(thread.getWordCount()));
            toWrite = toWrite.replaceAll("WSPM" , String.format("%-2.2f", thread.getWordsPerMessage()));
            toWrite = toWrite.replaceAll("CHPM" , String.format("%-2.2f", thread.getCharsPerMessage()));

            StringBuilder sb = new StringBuilder(toWrite);
            String user1ToAdd = "['" + thread.getUser() + "', " + String.valueOf(byUser) + "],\n";
            String user2ToAdd = "['" + partner + "', " + String.valueOf(byPartner) + "],\n";
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

    public static void genGroup(MessageThread thread) {

    }

    private static String fileToString(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
