package com.ori.design_pattern.create_type.factory;

import java.util.HashMap;
import java.util.Map;


/**
 * 工厂方法
 * 我们看到工厂方法本身和简单工厂很类似，之所以将某个代码块剥离出来，独立为函数或类，是这个代码块的逻辑过于复杂剥离之后更易维护
 * 使用场景：
 * 当对象的创建逻辑比较复杂，不只是简单的new一下就可以，将复杂的创建逻辑拆分到多个工厂类中，让每个工厂类都不至于过于复杂
 */
public class FactoryPattern {
    public IRuleConfigParser load(String ruleConfigFilePath) {
        IRuleConfigParserFactory parserFactory = null;
        if ("json".equalsIgnoreCase(ruleConfigFilePath)) {
            parserFactory = new JsonRuleConfigParserFactory();
        } else if ("xml".equalsIgnoreCase(ruleConfigFilePath)) {
            parserFactory = new XmlRuleConfigParserFactory();
        } else if ("yaml".equalsIgnoreCase(ruleConfigFilePath)) {
            parserFactory = new YamlRuleConfigParserFactory();
        } else if ("properties".equalsIgnoreCase(ruleConfigFilePath)) {
            parserFactory = new PropertiesRuleConfigParserFactory();
        }
        return parserFactory.createParser();
    }
}

/**
 * 工厂方法二
 */
class RuleConfigParserFactoryMap { //工厂的工厂
    private static final Map cachedFactories = new HashMap<>();

    static {
        cachedFactories.put("json", new JsonRuleConfigParserFactory());
        cachedFactories.put("xml", new XmlRuleConfigParserFactory());
        cachedFactories.put("yaml", new YamlRuleConfigParserFactory());
        cachedFactories.put("properties", new PropertiesRuleConfigParserFactory());
    }

    public static IRuleConfigParserFactory getParserFactory(String type) {
        if (type == null || type.isEmpty()) {
            return null;
        }
        IRuleConfigParserFactory parserFactory = (IRuleConfigParserFactory) cachedFactories.get(type.toLowerCase());
        return parserFactory;
    }
}


interface IRuleConfigParserFactory {
    IRuleConfigParser createParser();
}

class JsonRuleConfigParserFactory implements IRuleConfigParserFactory {
    @Override
    public IRuleConfigParser createParser() {
        return new JsonRuleConfigParser();
    }
}

class XmlRuleConfigParserFactory implements IRuleConfigParserFactory {
    @Override
    public IRuleConfigParser createParser() {
        return new XmlRuleConfigParser();
    }
}

class YamlRuleConfigParserFactory implements IRuleConfigParserFactory {
    @Override
    public IRuleConfigParser createParser() {
        return new YamlRuleConfigParser();
    }
}

class PropertiesRuleConfigParserFactory implements IRuleConfigParserFactory {
    @Override
    public IRuleConfigParser createParser() {
        return new PropertiesRuleConfigParser();
    }
}
