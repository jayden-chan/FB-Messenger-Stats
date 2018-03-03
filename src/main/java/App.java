/**
 * @author Jayden Chan
 * @version 1.0
 *
 * Main entry point for the program. Simply handles the command line arguments.
 */
public class App {

    private enum UsageType {
        ALL,
        PRINT,
        SINGLE,
        WRITE,
        COMPARE
    }

    public static void main(String[] args) {
        if(args.length == 0 || args[0].equals("--help")) {
            printUsage(UsageType.ALL);
        }

        switch(args[0]) {
            case "-p":
                handleP(args);
                break;
            case "-s":
                handleS(args);
                break;
            case "-w":
                handleW(args);
                break;
            case "-c":
                handleC(args);
                break;
            default:
                printUsage(UsageType.ALL);
                break;
        }
    }

    private static void handleP(String[] args) {
        if(args.length < 2) {
            printUsage(UsageType.PRINT);
            return;
        }

        MessageThread thread;

        try {
            thread = new MessageThread(args[1]);
            System.out.println(thread);
        }
        catch(IllegalArgumentException iae) {
            System.out.println(iae);
            return;
        }
    }

    private static void handleS(String[] args) {
        if(args.length < 2) {
            printUsage(UsageType.SINGLE);
            return;
        }
    }

    private static void handleW(String[] args) {
        if(args.length < 3) {
            printUsage(UsageType.WRITE);
            return;
        }
    }

    private static void handleC(String[] args) {
        if(args.length < 3) {
            printUsage(UsageType.COMPARE);
            return;
        }
    }

    /****************************************************************/
    /*                        Helper methods                        */
    /****************************************************************/

    private static void printUsage(UsageType type) {
        StringBuilder sb = new StringBuilder();
        sb.append("Usage:\n");

        switch(type) {
            case ALL:
                sb.append("    -p <file name>             Print the statistics for the given conversation thread.\n");
                sb.append("    -s <file name>             Generate an HTML file containing the statistics with fancy graphs and charts.\n");
                sb.append("    -w <file name> <format>    Parse the conversation thread and write it to disk. Formats: -txt, -csv\n\n");
                sb.append("    -c <start> <end>           Perform comparison statistics on the given conversation threads.\n");
                sb.append("                               Assumes that the threads are named with the default format (1.html, 2.html...)");
                break;
            case PRINT:
                sb.append("    -p <file name>             Print the statistics for the given conversation thread.\n");
                break;
            case SINGLE:
                sb.append("    -s <file name>             Generate an HTML file containing the statistics with fancy graphs and charts.\n");
                break;
            case WRITE:
                sb.append("    -w <file name> <format>    Parse the conversation thread and write it to disk. Formats: -txt, -csv\n\n");
                break;
            case COMPARE:
                sb.append("    -c <start> <end>           Perform comparison statistics on the given conversation threads.\n");
                sb.append("                               Assumes that the threads are named with the default format (1.html, 2.html...)");
                break;
        }

        System.out.println(sb.toString());
    }
}
