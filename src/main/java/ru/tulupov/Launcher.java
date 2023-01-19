package ru.tulupov;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the starting point for running the program.
 */
public class Launcher {
    /**
     * This variable serves as a flag for switching the sorting (ascending or descending) mode.
     */
    static boolean isAscending = true;
    /**
     * This variable serves as a flag for switching the data type may be string or integer.
     */
    static boolean isStrings = true;
    /**
     * This variable stores the name of the output file.
     */
    static String outputFileName = "";
    /**
     * This list stores the names of the input files.
     */
    static List<String> inputFileNames = new ArrayList<>();

    public static void main(String[] args) {

        new ParseCommandLine(args).parse();

        Mediator mediator = new MediatorImpl();
        try (Sorter sorter = new SorterImpl(outputFileName);
             WorkersHolder holder = new WorkersHolder(sorter,mediator)) {
            mediator.subscribe((MediatorsSubscriber) sorter);
            holder.start();
        } catch (IOException e) {
            System.err.println ("Problem to close file" + e);
        }
    }
}
