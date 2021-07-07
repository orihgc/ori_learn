package com.ori.design_pattern.create_type.singleton;

/**
 * 静态内部类
 * 延迟加载
 * instance 的唯一性、创建过程的线程安全性，都由 JVM 来保证
 */
public class StaticInnerClass {

    private StaticInnerClass() {

    }

    private static class SingleHolder {
        private static final StaticInnerClass instance = new StaticInnerClass();
    }

    public static StaticInnerClass getInstance() {
        return SingleHolder.instance;
    }
}
