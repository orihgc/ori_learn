package com.ori.design_pattern.behavioral_type.responsibility;

/**
 * 在 GoF 给出的定义中，如果处理器链上的某个处理器能够处理这个请求，那就不会继续往下传递请求。
 * 实际上，职责链模式还有一种变体，那就是请求会被所有的处理器都处理一遍，不存在中途终止的情况。
 * */
public class ResponsibilityPattern2 {
    public static void main(String[] args) {
        HandlerChain1 chain = new HandlerChain1();
        chain.addHandler(new HandlerA1());
        chain.addHandler(new HandlerB1());
        chain.handle();
    }
}


abstract class Handler1 {
    protected Handler1 successor = null;

    public void setSuccessor(Handler1 successor) {
        this.successor = successor;
    }

    public final void handle() {
        doHandle();
        if (successor != null) {
            successor.handle();
        }
    }

    protected abstract void doHandle();
}

class HandlerA1 extends Handler1 {
    @Override
    protected void doHandle() {
        //...
    }
}

class HandlerB1 extends Handler1 {
    @Override
    protected void doHandle() {
        //...
    }
}

class HandlerChain1 {
    private Handler1 head = null;
    private Handler1 tail = null;

    public void addHandler(Handler1 handler) {
        handler.setSuccessor(null);

        if (head == null) {
            head = handler;
            tail = handler;
            return;
        }

        tail.setSuccessor(handler);
        tail = handler;
    }

    public void handle() {
        if (head != null) {
            head.handle();
        }
    }
}





// HandlerChain和Application代码不变
