package com.ori.design_pattern.strutural_type.proxy;

import com.ori.design_pattern.strutural_type.proxy.original.IUserController;
import com.ori.design_pattern.strutural_type.proxy.original.MetricsCollector;
import com.ori.design_pattern.strutural_type.proxy.original.RequestInfoHelper;
import com.ori.design_pattern.strutural_type.proxy.original.UserVo;


/**
 * 代理模式
 * 原理：在不改变原始类代码的情况下，通过引入代理类来给原始类附加功能，原始类和代理类实现相同的接口，是基于接口而非实现编程
 */
public class ProxyPattern {
    public static void main(String[] args) {
        //UserControllerProxy使用举例
        //因为原始类和代理类实现相同的接口，是基于接口而非实现编程
        //将UserController类对象替换为UserControllerProxy类对象，不需要改动太多代码
        IUserController userController = new UserControllerProxy(new UserController());
    }
}


class UserController implements IUserController {
    //...省略其他属性和方法...

    @Override
    public UserVo login(String telephone, String password) {
        //...省略login逻辑...
        //...返回UserVo数据...
        return null;
    }

    @Override
    public UserVo register(String telephone, String password) {
        //...省略register逻辑...
        //...返回UserVo数据...
        return null;
    }
}

class UserControllerProxy implements IUserController {
    private MetricsCollector metricsCollector;
    private UserController userController;

    public UserControllerProxy(UserController userController) {
        this.userController = userController;
        this.metricsCollector = new MetricsCollector();
    }

    @Override
    public UserVo login(String telephone, String password) {
        long startTimestamp = System.currentTimeMillis();

        // 委托
        UserVo userVo = userController.login(telephone, password);

        long endTimeStamp = System.currentTimeMillis();
        long responseTime = endTimeStamp - startTimestamp;
        RequestInfoHelper requestInfoHelper = new RequestInfoHelper("login", responseTime, startTimestamp);
        metricsCollector.recordRequest(requestInfoHelper);

        return userVo;
    }

    @Override
    public UserVo register(String telephone, String password) {
        long startTimestamp = System.currentTimeMillis();

        UserVo userVo = userController.register(telephone, password);

        long endTimeStamp = System.currentTimeMillis();
        long responseTime = endTimeStamp - startTimestamp;
        RequestInfoHelper requestInfoHelper = new RequestInfoHelper("register", responseTime, startTimestamp);
        metricsCollector.recordRequest(requestInfoHelper);

        return userVo;
    }
}







