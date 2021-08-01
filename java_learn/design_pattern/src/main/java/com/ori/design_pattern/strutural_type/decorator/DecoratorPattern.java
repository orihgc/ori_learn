package com.ori.design_pattern.strutural_type.decorator;

/**
 * 装饰器模式
 * 原始类和装饰器类都继承同样的父类，这样可以对原始类嵌套多个装饰器类
 * 作用：装饰器模式附加的是和原始类相关的增强功能。而代理模式附加的是和原始类无关的功能
 */
public class DecoratorPattern {
    public static void main(String[] args) {
        FileInputStream fileInputStream = new FileInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);
    }
}

abstract class InputStream {
}

class FileInputStream extends InputStream {
    public FileInputStream() {

    }
}

class BufferedInputStream extends InputStream {
    protected volatile InputStream in;

    public BufferedInputStream(InputStream in) {
        this.in = in;
    }
}

class DataInputStream extends InputStream {
    protected volatile InputStream in;

    public DataInputStream(InputStream in) {
        this.in = in;
    }
}
