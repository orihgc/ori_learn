package com.ori.design_pattern.create_type.factory;


/**
 * 抽象工厂
 * 必要元素：
 * 1、抽象工厂，多个抽象方法（生产产品）
 * 2、抽象产品，多个实际产品
 * 3、实际工厂
 * 4、实际产品
 * */

public abstract class AbstractFactory {
    public abstract Color getColor(String color);

    public abstract Shape getShape(String shape);
}

class FactoryProducer {
    public static AbstractFactory getFactory(String choice) {
        if (choice.equalsIgnoreCase("SHAPE")) {
            return new ShapeFactory();
        } else if (choice.equalsIgnoreCase("COLOR")) {
            return new ColorFactory();
        }
        return null;
    }
}

class ShapeFactory extends AbstractFactory {

    @Override
    public Shape getShape(String shapeType) {
        if (shapeType == null) {
            return null;
        }
        if (shapeType.equalsIgnoreCase("CIRCLE")) {
            return new Circle();
        } else if (shapeType.equalsIgnoreCase("RECTANGLE")) {
            return new Rectangle();
        } else if (shapeType.equalsIgnoreCase("SQUARE")) {
            return new Square();
        }
        return null;
    }

    @Override
    public Color getColor(String color) {
        return null;
    }
}

class ColorFactory extends AbstractFactory {

    @Override
    public Shape getShape(String shapeType) {
        return null;
    }

    @Override
    public Color getColor(String color) {
        if (color == null) {
            return null;
        }
        if (color.equalsIgnoreCase("RED")) {
            return new Red();
        } else if (color.equalsIgnoreCase("GREEN")) {
            return new Green();
        } else if (color.equalsIgnoreCase("BLUE")) {
            return new Blue();
        }
        return null;
    }
}

/**
 * 形状
 */
interface Shape {
    void draw();
}

class Rectangle implements Shape {
    @Override
    public void draw() {
        System.out.println("draw rect");
    }
}

class Square implements Shape {
    @Override
    public void draw() {
        System.out.println("draw square");
    }
}

class Circle implements Shape {
    @Override
    public void draw() {
        System.out.println("draw circle");
    }
}

/**
 * 颜色
 */
interface Color {
    void fill();
}

class Red implements Color {

    @Override
    public void fill() {
        System.out.println("fill red");
    }
}

class Green implements Color {

    @Override
    public void fill() {
        System.out.println("fill green");
    }
}

class Blue implements Color {

    @Override
    public void fill() {
        System.out.println("fill blue");
    }
}