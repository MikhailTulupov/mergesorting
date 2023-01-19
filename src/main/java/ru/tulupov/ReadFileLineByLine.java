package ru.tulupov;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * This class read file line by line.
 */
public class ReadFileLineByLine implements Runnable, Closeable {
    private FileInputStream inputStream;
    private Scanner scanner;
    private BlockingDeque<String> queue;
    private ExecutorService service;
    /**
     * This boolean variable notifies us if there is no read access to the file.
     */
    private boolean failed;
    private final Mediator mediator;

    public ReadFileLineByLine(String filename, Mediator mediator) {
        this.mediator = mediator;
        try {
            inputStream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            failed = true;
            return;
        }

        scanner = new Scanner(inputStream);
        queue = new LinkedBlockingDeque<>(2);
        service = Executors.newSingleThreadExecutor();
    }

    /**
     * Method notify if there is no read access to the file
     *
     * @return false if file can't open for read
     */
    public boolean isFailed() {
        return failed;
    }

    /**
     * Method Submits a Runnable task for execution and returns a BlockingDeque that task.
     *
     * @return BlockingDeque with string.
     */
    public BlockingDeque<String> beginAsyncReading() {
        if (!failed) {
            service.submit(this);
            return queue;
        } else {
            return null;
        }
    }

    @Override
    public void run() {
        try {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line == null) continue;
                String value = line.replaceAll("\\s+", "");
                try {
                    queue.putLast(value);
                } catch (InterruptedException e) {
                    System.err.println("Trouble read file" + e.getMessage());
                }
            }

        } finally {
            close();
        }
    }

    @Override
    public void close() {
        if (scanner != null) scanner.close();
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException ignored) {

            }
        }
        mediator.notifyEof(queue);
        service.shutdownNow();
    }

}
