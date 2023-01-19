package ru.tulupov;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;

/**
 * ru.tulupov.Mediator implementation.
 */
public class MediatorImpl implements Mediator {
    private final List<MediatorsSubscriber> allReceivers = new ArrayList<>();

    @Override
    public synchronized void notifyEof(BlockingDeque<String> deque) {
        for (MediatorsSubscriber subscriber : allReceivers){
            subscriber.notify(deque);
        }
    }

    public void subscribe(MediatorsSubscriber subscriber){
        allReceivers.add(subscriber);
    }
}
