package com.ori.design_pattern.behavioral_type.observer.custom_event_bus;

import java.util.concurrent.Executor;

/**
 * AsyncEventBus的
 * */
public class AsyncEventBus extends EventBus {
    public AsyncEventBus(Executor executor) {
        super(executor);
    }
}
