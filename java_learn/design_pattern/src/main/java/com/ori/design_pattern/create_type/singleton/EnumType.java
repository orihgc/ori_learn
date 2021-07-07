package com.ori.design_pattern.create_type.singleton;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 枚举式
 */
public enum EnumType {
    INSTANCE;

    private AtomicInteger id = new AtomicInteger(0);

    public Integer getId() {
        return id.incrementAndGet();
    }
}
