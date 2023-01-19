package ru.tulupov;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class working of input files. {@code ru.tulupov.WorkersHolder}
 * class contains workers each other witch work.
 */
public class WorkersHolder implements Closeable {
    /**
     * This list contains blocking deques with strings from files
     */
    private final List<BlockingDeque<String>> deques = new CopyOnWriteArrayList<>();
    /**
     * This list contains workers witch read files
     */
    private final List<ReadFileLineByLine> workers = new ArrayList<>();
    /**
     * This variable contains implement {@code ru.tulupov.Sorter} interface
     */
    private final Sorter sorter;
    /**
     * This variable contains implement {@code ru.tulupov.Mediator} interface
     */
    private final Mediator mediator;


    public WorkersHolder(Sorter sorter, Mediator mediator) {
        this.sorter = sorter;
        this.mediator = mediator;
    }

    public void start() {
        for (String file : Launcher.inputFileNames) {
            ReadFileLineByLine worker = new ReadFileLineByLine(file, mediator);
            if (!worker.isFailed()) workers.add(worker);
        }
        if (workers.size() == 0) {
            System.exit(9);
        } else {
            for (ReadFileLineByLine reader : workers) {
                BlockingDeque<String> deque = reader.beginAsyncReading();
                if (deque != null) deques.add(deque);
            }
        }
        sorter.sort(deques);
    }
    @Override
    public void close() {
        workers.forEach(ReadFileLineByLine::close);
    }
}
