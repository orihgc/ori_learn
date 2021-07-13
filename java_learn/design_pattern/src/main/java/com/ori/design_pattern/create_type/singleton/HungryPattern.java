package com.ori.design_pattern.create_type.singleton;

/**
 * 饿汉式单例模式
 * 优点：线程安全。由于静态实例创建并初始化好了
 * 缺点：不支持延迟加载
 */
public class HungryPattern {
    private static final HungryPattern instance = new HungryPattern();

    private HungryPattern() {
    }

    public static HungryPattern getInstance() {
        return instance;
    }
}
