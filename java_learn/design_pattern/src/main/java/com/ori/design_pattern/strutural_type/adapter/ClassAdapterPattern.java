package com.ori.design_pattern.strutural_type.adapter;

/**
 * 类适配器模式
 * 如果Adaptee接口很多，而且Adaptee和ITarget接口定义大部分都相同
 * 作用：封装有缺陷的接口设计，
 */
public class ClassAdapterPattern {
}

/**
 * 要转化的接口定义
 */
interface ITarget {
    void f1();

    void f2();
}

/**
 * 不兼容ITarget接口定义的接口
 */
class Adaptee {
    public void fa() { }

    public void fb() { }
}

class ClassAdapter extends Adaptee implements ITarget {

    @Override
    public void f1() {
        super.fa();
    }

    @Override
    public void f2() {
        super.fb();
    }
}

