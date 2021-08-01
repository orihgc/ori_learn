package com.ori.design_pattern.strutural_type.adapter;

public class ReplaceDependentExternalSystem {
    public static void main(String[] args) {
        Demo demo = new Demo(new BAdapter(new B()));
    }
}

interface IA {
    void fa();
}

class A implements IA {

    @Override
    public void fa() {

    }
}

class Demo {
    private IA ia;

    public Demo(IA ia) {
        this.ia = ia;
    }
}

class B implements IA{

    @Override
    public void fa() {

    }
}

class BAdapter implements IA {

    private B b;

    public BAdapter(B b) {
        this.b = b;
    }

    @Override
    public void fa() {
        b.fa();
    }
}
