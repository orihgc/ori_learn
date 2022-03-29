package com.ori.state_learn;

public class Test {
    public static void main(String[] args) {
        Test test = new Test();
        System.out.println(test);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
