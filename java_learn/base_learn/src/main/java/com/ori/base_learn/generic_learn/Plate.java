package com.ori.base_learn.generic_learn;

import java.util.ArrayList;
import java.util.List;

interface Plate<T> {
    void set(T t);

    T get();
}

class AIPlate<T extends Comparable> implements Plate<T> {

    private final List<T> fruitList = new ArrayList<>();

    @Override
    public void set(T t) {
        fruitList.add(t);
    }

    @Override
    public T get() {
        return fruitList.get(0);
    }

    public AIPlate<T> getAIPlate() {
        return this;
    }
}

class Person{
    public static void main(String[] args) {

    }
}



