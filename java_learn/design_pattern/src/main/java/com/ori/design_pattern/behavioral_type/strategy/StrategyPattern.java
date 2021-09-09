package com.ori.design_pattern.behavioral_type.strategy;

import java.util.HashMap;
import java.util.Map;

public class StrategyPattern {
}

/**
 * 策略定义
 */
interface Strategy {
    void algorithmInterface();
}

class ConcreteStrategyA implements Strategy {
    @Override
    public void algorithmInterface() {
        //具体的算法...
    }
}

class ConcreteStrategyB implements Strategy {
    @Override
    public void algorithmInterface() {
        //具体的算法...
    }
}

/**
 * 策略的创建
 * */
//
class StrategyFactory {
    private static final Map<String, Strategy> strategies = new HashMap<>();

    static {
        strategies.put("A", new ConcreteStrategyA());
        strategies.put("B", new ConcreteStrategyB());
    }

    public static Strategy getStrategy(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("type should not be empty.");
        }
        return strategies.get(type);
    }
}


class StrategyFactory2 {
    public static Strategy getStrategy(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("type should not be empty.");
        }

        if (type.equals("A")) {
            return new ConcreteStrategyA();
        } else if (type.equals("B")) {
            return new ConcreteStrategyB();
        }

        return null;
    }
}
