package com.ori.design_pattern.strutural_type.adapter;

/**
 * 对象适配器
 * 如果Adaptee接口很多，而且Adaptee和Itarget接口定义大部分都不相同。推荐使用对象适配器
 * */
public class ObjectAdapterPattern {
}

interface Target {
    void f1();

    void f2();
}

class Adapt {
    public void fa() {
    }

    public void fb() {
    }
}

class ObjectAdapter implements Target {
    private Adaptee adaptee;

    public ObjectAdapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void f1() {

    }

    @Override
    public void f2() {

    }
}
