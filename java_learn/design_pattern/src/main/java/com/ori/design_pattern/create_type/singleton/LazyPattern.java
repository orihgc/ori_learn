package com.ori.design_pattern.create_type.singleton;

/**
 * 懒汉式
 * 优点：支持延迟加载
 * 缺点：并发度低。这里给getInstance加了把大锁
 */
public class LazyPattern {
    private static LazyPattern instance;

    private LazyPattern() {
    }

    public static synchronized LazyPattern getInstance() {
        if (instance == null) {
            instance = new LazyPattern();
        }
        return instance;
    }
}
