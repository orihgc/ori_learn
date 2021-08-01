package com.ori.design_pattern.strutural_type.decorator;


interface Primitive {
    void fun();
}

class DecoratorParent implements Primitive {
    Primitive primitive;

    public DecoratorParent(Primitive primitive) {
        this.primitive = primitive;
    }

    @Override
    public void fun() {
    }
}

class Decorator1 extends DecoratorParent {
    public Decorator1(Primitive primitive) {
        super(primitive);
    }
}

class Decorator2 extends DecoratorParent {

    public Decorator2(Primitive primitive) {
        super(primitive);
    }
}

