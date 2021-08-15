package com.ori.design_pattern.behavioral_type.template;

/**
 * 相对于普通的函数调用来说，回调是一种双向调用关系。A 类事先注册某个函数 F 到 B 类，A 类在调用 B 类的 P 函数的时候，B 类反过来调用 A 类注册给它的 F 函数。
 * 这里的 F 函数就是“回调函数”。A 调用 B，B 反过来又调用 A，这种调用机制就叫作“回调”。
 *
 * 分类：
 * 同步回调：指在函数返回之前执行回调函数。看起来更新模板模式
 * 异步回调：指在函数返回之后执行回调函数。看起来更像观察者模式
 * */
public class CallBackDemo {
    public static void main(String[] args) {
        BClass b = new BClass();
        b.process(new ICallback() { //回调对象
            @Override
            public void methodToCallback() {
                System.out.println("Call back me.");
            }
        });
    }
}


interface ICallback {
    void methodToCallback();
}

class BClass {
    public void process(ICallback callback) {
        //...
        callback.methodToCallback();
        //...
    }
}

