package com.ori.design_pattern.create_type.singleton;


/**
 * 双重检测
 * volatile: 由于指令重排，导致DoubleDetectionType被实例化后并且赋值到instance后，还没来得及初始化，就被另一个线程占用了
 *           然而在高版本的JDK中已经解决了这个问题
 * */
public class DoubleDetectionPattern {
    private static volatile DoubleDetectionPattern instance;

    private DoubleDetectionPattern() {
    }

    public static DoubleDetectionPattern getInstance() {
        if (instance == null) {
            synchronized (DoubleDetectionPattern.class) {
                if (instance == null) {
                    instance = new DoubleDetectionPattern();
                }
            }
        }
        return instance;
    }
}
