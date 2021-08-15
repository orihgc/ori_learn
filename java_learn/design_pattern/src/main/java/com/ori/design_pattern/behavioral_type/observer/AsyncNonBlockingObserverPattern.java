package com.ori.design_pattern.behavioral_type.observer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class AsyncNonBlockingObserverPattern {
    public static void main(String[] args) {

    }
}

/**
 * 异步非阻塞观察者模式的简易实现
 */
/*
 * 第一种是在每个处理消息的函数中新建一个线程执行代码逻辑
 * 缺点：频繁创建和销毁线程比较耗时，切并发数量无法控制，创建过多的线程会导致堆栈溢出
 * */
class ConcreteObserver3 implements Observer {
    @Override
    public void update(Message message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("ConcreteObserver3 notified");
            }
        }).start();
    }
}

/*
 * 第二种是在通知每个观察者的时候使用线程池来执行每个观察者的处理消息的方法
 * 虽然利用线程池解决了第一种实现方式的问题，但是线程池、异步执行逻辑都耦合在notifyObservers函数中，增加了这部分代码的维护成本
 * */
class ConcreteSubject2 implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private Executor executor;

    ConcreteSubject2(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(final Message message) {
        for (final Observer observer : observers) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    observer.update(message);
                }
            });
        }
    }
}

/**
 * EventBus
 * 作用：不仅支持异步非阻塞模式，同时支持同步阻塞模式
 * 区别：EventBus，我们不需定义Observer接口，任意类型的对象都可以注册到EventBus中，
 * 通过@Subscribe注解标记类中哪个函数可以接受被观察者发送的消息
 */
class ConcreteSubject3 implements Subject {

    private EventBus eventBus;

    public ConcreteSubject3() {
//        eventBus = new EventBus();//同步阻塞模式
    }

    @Override
    public void registerObserver(Observer observer) {
        eventBus.register(observer);
    }

    @Override
    public void removeObserver(Observer observer)
    {
        eventBus.unregister(observer);
    }

    @Override
    public void notifyObservers(Message message) {
        eventBus.post(message);
    }
}

class ConcreteObserver4 {
    @Subscribe
    public void update(Message message) {
    }
}



