package com.ori.base_learn.generic_learn;

import java.util.ArrayList;
import java.util.List;

class Animal {
}

class Cat extends Animal {

}

class BlackCat extends Cat {

}

class AnimalMain {
    public static void main(String[] args) {
        List<? extends Animal> animals = new ArrayList<Cat>();

    }
}
