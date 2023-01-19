package ru.tulupov;

import java.util.concurrent.BlockingDeque;

/**
 * A {@code ru.tulupov.Mediator} is this intermediary between objects implements the mediator pattern.
 */
public interface Mediator {
    /**
     * Method notify subscriber of end of file.
     * @param deque deque
     */
    void notifyEof(BlockingDeque<String> deque);

    /**
     * Method subscribe new object.
     * @param subscriber subscriber
     */
    void subscribe(MediatorsSubscriber subscriber);
}
