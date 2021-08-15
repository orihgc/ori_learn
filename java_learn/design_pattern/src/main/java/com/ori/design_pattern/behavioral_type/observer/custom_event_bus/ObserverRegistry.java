package com.ori.design_pattern.behavioral_type.observer.custom_event_bus;


import com.google.common.base.Preconditions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class ObserverRegistry {
    private ConcurrentMap<Class<?>, CopyOnWriteArraySet<ObserverAction>> registry = new ConcurrentHashMap<>();

    /**
     * 注册一个观察者
     * */
    public void register(Object observer) {
        Map<Class<?>, Collection<ObserverAction>> observerActions = findAllObserverActions(observer);
        for (Map.Entry<Class<?>, Collection<ObserverAction>> entry : observerActions.entrySet()) {
            //获取事件类型
            Class<?> eventType = entry.getKey();
            //获取对应事件类型的可接受函数的列表
            Collection<ObserverAction> eventActions = entry.getValue();
            //获取已经存好的事件类型对应的可接受函数列表
            CopyOnWriteArraySet<ObserverAction> registeredEventActions = registry.get(eventType);
            //若原本列表为空
            if (registeredEventActions == null) {
                //为对应事件类型新建CopyOnWriteArraySet
                registry.putIfAbsent(eventType, new CopyOnWriteArraySet<ObserverAction>());
                registeredEventActions = registry.get(eventType);
            }
            //将所有扫描到的可接受函数列表添加到对应事件类型的CopyOnWriteArraySet中去
            registeredEventActions.addAll(eventActions);
        }
    }

    //返回对应事件类型的所有可接受函数
    public List<ObserverAction> getMatchedObserverActions(Object event) {
        List<ObserverAction> matchedObservers = new ArrayList<>();
        //获取事件类型
        Class<?> postedEventType = event.getClass();
        for (Map.Entry<Class<?>, CopyOnWriteArraySet<ObserverAction>> entry : registry.entrySet()) {
            Class<?> eventType = entry.getKey();
            Collection<ObserverAction> eventActions = entry.getValue();
            //从Observer注册表中匹配到相关事件类型，进行事件类型匹配
            if (postedEventType.isAssignableFrom(eventType)) {
                matchedObservers.addAll(eventActions);
            }
        }
        return matchedObservers;
    }

    /**
     * 找到所有的可接受函数
     * */
    private Map<Class<?>, Collection<ObserverAction>> findAllObserverActions(Object observer) {
        Map<Class<?>, Collection<ObserverAction>> observerActions = new HashMap<>();
        Class<?> clazz = observer.getClass();
        //遍历带Subscribe注解的方法
        for (Method method : getAnnotatedMethods(clazz)) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?> eventType = parameterTypes[0];//获取事件类型
            //没有该事件类型，则新建
            if (!observerActions.containsKey(eventType)) {
                observerActions.put(eventType, new ArrayList<ObserverAction>());
            }
            //将可接受函数映射到对应事件类型中去
            observerActions.get(eventType).add(new ObserverAction(observer, method));
        }
        return observerActions;
    }

    /**
     * 获取一个类中所有带Subscribe注解的方法
     * 必须正好只有一个参数
     * */
    private List<Method> getAnnotatedMethods(Class<?> clazz) {
        List<Method> annotatedMethods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                //必须正好只有一个参数
                Preconditions.checkArgument(parameterTypes.length == 1, "Method %s has @Subscribe annotation but has %s parameters." + "Subscriber methods must have exactly 1 parameter.", method, parameterTypes.length);
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }
}
