package ru.tulupov;

import org.apache.commons.cli.*;

import java.util.List;

/**
 * This class parses the command line and splits it into options and filenames.
 */
class ParseCommandLine {
    /**
     * This variable contains command line arguments
     */
    private final String[] args;

    /**
     * Constructs command line parser.
     *
     * @param args arguments
     */
    ParseCommandLine(String... args) {
        this.args = args;
    }

    /**
     * Method print usage information.
     *
     * @param options option
     * @param status  exit status code
     */
    private static void printHelpAndShutdown(Options options, int status) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("""
                java -jar filesorter.jar [OPTIONS] output.file input.files...
                output.file  Required file name with the sorting result.
                input.files  One or more input files.
                """, options);
        System.exit(status);
    }

    /**
     * Method parse command line.
     */
    void parse() {
        Options options = new Options();
        options.addOption("s", false, "The files contain strings. Mandatory, " +
                "mutually exclusive with -i.");
        options.addOption("i", false, "The files contain integers. Mandatory, " +
                "mutually exclusive with -s.");
        options.addOption("a", false, "Sort in ascending order. " +
                "It is applied by default if -a or -d is missing.");
        options.addOption("d", false, "Sort in descending order. " +
                "The option is optional, as is -a.");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (UnrecognizedOptionException e) {
            printHelpAndShutdown(options, 1);
        } catch (ParseException e) {
            printHelpAndShutdown(options, 2);
        }
        if (cmd == null) {
            printHelpAndShutdown(options, 3);
        } else {
            if (!(cmd.hasOption('i') || cmd.hasOption('s'))) {
                printHelpAndShutdown(options, 4);
            }
            if (cmd.hasOption('i') && cmd.hasOption('s')) {
                printHelpAndShutdown(options, 5);
            }
            if (cmd.hasOption('a') && cmd.hasOption('d')) {
                printHelpAndShutdown(options, 6);
            }

            List<String> files = cmd.getArgList();
            if (files.size() < 2) {
                printHelpAndShutdown(options, 7);
            }

            if (cmd.hasOption('d')) Launcher.isAscending = false;
            if (cmd.hasOption('i')) Launcher.isStrings = false;
            Launcher.outputFileName = files.get(0);
            files.remove(0);
            Launcher.inputFileNames = files;

        }
    }
}