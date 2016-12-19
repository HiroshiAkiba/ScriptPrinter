import java.io.*;
import java.lang.ProcessBuilder;
import java.util.ArrayList;
import java.util.Scanner;

public class TestScriptCaller {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public static void main(String[] args) throws IOException, InterruptedException {

        // Prints the names of all the translators in that are available
        System.out.println(ANSI_CYAN + "\nThe following translators are available to run:" + ANSI_RESET);
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", "ls");
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.directory(new File("Translators"));
        Process p = pb.start();
        p.waitFor();

        // Prompts the user for a specific translator to use
        System.out.println(ANSI_CYAN + "\nWhich translator would you like to run?" + ANSI_RESET);
        Scanner translatorNameScanner = new Scanner(System.in);
        String translatorName = "Translators/" + translatorNameScanner.next();

        // Prints the names of the boot commands available in this translator
        System.out.println(ANSI_CYAN + "\nThe following boot commands are available to run:" + ANSI_RESET);

        ArrayList<String> commandList = new ArrayList<String>();

        File file = new File(translatorName + "/BootCommands.txt");
        Scanner commands = new Scanner(file);
        commands.useDelimiter("[\\p{javaWhitespace}()\"]+");

        while(commands.hasNext()) {
            if(commands.next().equals("bool")) {
                commandList.add(commands.next());
            }
        }
        for(String s: commandList) {
            System.out.println(s);
        }

        // Prompts the user for a specific boot command to run in SBT
        System.out.println(ANSI_CYAN + "\nWhich command from this translator would you like to run?" + ANSI_RESET);
        Scanner commandNameScanner = new Scanner(System.in);
        String commandName = commandNameScanner.next();

        System.out.println(ANSI_CYAN + "\nWhich test would you like to run it on? (Give test number in 3-digit number. Eg 014)" + ANSI_RESET);
        Scanner testNumScanner = new Scanner(System.in);
        String testNum = testNumScanner.next();

        // The three strings used to execute the SBT command
        String runxtc = "runxtc";
        String command = "-" + commandName;
        String testFile = "src/test/java/inputs/test" + testNum + "/Test" + testNum + ".java";

        String entireCommand = runxtc + " " + command + " " + testFile;


        // Running SBT on the chosen translator
        ProcessBuilder pb2 = pb.command("bash", "-c", "javac ReprintString.java ; java ReprintString");
        pb2.directory(new File(translatorName + "/ReprintString/src"));
        pb2.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        Process p2 = pb2.start();

        //Give the process the following input.
//        OutputStream stdin = p2.getOutputStream();
//        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
//
//        writer.write(entireCommand);
//        writer.flush();
//        writer.close();

        p2.waitFor();


        /** p2 will hang and wait for input in the form of the string right above this section of the code (entireCommand).
         I need to pass the command in the form of a String above to p2 in order for it to execute. **/

    }
}
