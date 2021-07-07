package com.ori.design_pattern.create_type.singleton;

/**
 * 懒汉式
 * 优点：支持延迟加载
 * 缺点：并发度低。这里给getInstance加了把大锁
 */
public class LazyType {
    private static LazyType instance;

    private LazyType() {
    }

    public static synchronized LazyType getInstance() {
        if (instance == null) {
            instance = new LazyType();
        }
        return instance;
    }
}
