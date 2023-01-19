package ru.tulupov;

import java.util.concurrent.BlockingDeque;

/**
 * This {@code ru.tulupov.MediatorsSubscriber} interface notify subscriber of changes.
 */
public interface MediatorsSubscriber {
    /**
     * Method notify subscriber of changes.
     * @param deque deque
     */
    void notify(BlockingDeque<String> deque);
}
