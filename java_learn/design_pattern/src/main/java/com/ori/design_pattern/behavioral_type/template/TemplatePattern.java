package com.ori.design_pattern.behavioral_type.template;

/**
 * 模板模式
 * 模板方法模式将一个方法定义为一个算法骨架，并将某些步骤推迟到子类中实现
 * 模板方法可以让子类在不改变算法整体结构的情况下，重新定义算法中的某些步骤
 * 作用一：复用
 * 模板模式把一个算法中不变的流程抽象到父类的模板方法 templateMethod() 中，将可变的部分 method1()、method2() 留给子类 ContreteClass1 和 ContreteClass2 来实现。
 * 所有的子类可以复用父类中提供的模板方法的代码
 * 作用二：扩展
 * 有点类似于控制反转
 * 框架通过模板模式提供功能扩展点，让框架用户可以在不修改框架源码的情况下，基于扩展点定制化框架的功能。
 * */
public class TemplatePattern {
    public static void main(String[] args) {
        AbstractClass demo = new ConcreteClass1();
        demo.templateMethod();
    }
}

/*
* 在模板模式经典的实现中，模板方法定义为 final，可以避免被子类重写。
* 需要子类重写的方法定义为 abstract，可以强迫子类去实现。
* 不过，在实际项目开发中，模板模式的实现比较灵活，以上两点都不是必须的。
* */
abstract class AbstractClass {
    public final void templateMethod() {
        //...
        method1();
        //...
        method2();
        //...
    }

    protected abstract void method1();

    protected abstract void method2();
}

class ConcreteClass1 extends AbstractClass {
    @Override
    protected void method1() {
        //...
    }

    @Override
    protected void method2() {
        //...
    }
}

class ConcreteClass2 extends AbstractClass {
    @Override
    protected void method1() {
        //...
    }

    @Override
    protected void method2() {
        //...
    }
}
