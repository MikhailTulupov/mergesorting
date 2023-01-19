package ru.tulupov;

import java.io.Closeable;
import java.util.List;
import java.util.concurrent.BlockingDeque;

/**
 * A {@code ru.tulupov.Sorter} is data sorter interface
 */
public interface Sorter extends Closeable {
    /**
     * Method sort data.
     * @param dequeList deque list data.
     */
    void sort(List<BlockingDeque<String>> dequeList);
}
