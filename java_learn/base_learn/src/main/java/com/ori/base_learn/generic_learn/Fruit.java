package com.ori.base_learn.generic_learn;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class Fruit {
}

class Apple extends Fruit {

}

class RedApple extends Apple {

}

class FruitMain {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        FruitMain fruitMain = new FruitMain();

        fruitMain.test2();
    }

    void test1() {
        List<? extends Fruit> fruits = new ArrayList<>();
        //fruits.add(new Apple());
        try {
            Method add = fruits.getClass().getMethod("add", Object.class);
            add.invoke(fruits, new Apple());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ori");
    }

    void test2() {
        List<? super Apple> list = new ArrayList<>();
//        list.add(new Fruit());
        list.add(new Apple()); 
        list.add(new RedApple());
        System.out.println("ori");
    }
}
