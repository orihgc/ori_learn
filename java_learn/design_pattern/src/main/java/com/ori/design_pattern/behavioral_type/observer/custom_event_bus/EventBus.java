package com.ori.design_pattern.behavioral_type.observer.custom_event_bus;

import com.google.common.util.concurrent.MoreExecutors;

import java.util.List;
import java.util.concurrent.Executor;

public class EventBus {
    private Executor executor;
    private ObserverRegistry registry = new ObserverRegistry();

    public EventBus() {
        this(MoreExecutors.directExecutor());
    }

    public EventBus(Executor executor) {
        this.executor = executor;
    }

    public void register(Object object) {
        registry.register(object);
    }

    public void post(final Object event) {
        //获取到所有可接收该事件类型的函数的列表
        List<ObserverAction> observerActions = registry.getMatchedObserverActions(event);
        //遍历执行这些扫描到的函数
        for (final ObserverAction observerAction : observerActions) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    observerAction.execute(event);
                }
            });
        }
    }
}
