package com.ori.design_pattern.behavioral_type.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 同步阻塞的实现方式，最经典的实现方式
 * 作用：主要是为了代码解耦
 */
public class SyncBlockingObserverPattern {
    public static void main(String[] args) {
        ConcreteSubject subject = new ConcreteSubject();
        subject.registerObserver(new ConcreteObserver1());
        subject.registerObserver(new ConcreteObserver2());
        subject.notifyObservers(new Message());
    }
}

interface Subject {
    void registerObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers(Message message);
}

interface Observer {
    void update(Message message);
}

class Message {
}

class ConcreteSubject implements Subject {

    private List<Observer> observers = new ArrayList<>();

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Message message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}

class ConcreteObserver1 implements Observer {

    @Override
    public void update(Message message) {
        System.out.println("Observer 1 is notified");
    }
}

class ConcreteObserver2 implements Observer {

    @Override
    public void update(Message message) {
        System.out.println("Observer 1 is notified");
    }
}