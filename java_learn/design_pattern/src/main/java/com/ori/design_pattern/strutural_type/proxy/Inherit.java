package com.ori.design_pattern.strutural_type.proxy;

import com.ori.design_pattern.strutural_type.proxy.original.MetricsCollector;
import com.ori.design_pattern.strutural_type.proxy.original.RequestInfoHelper;
import com.ori.design_pattern.strutural_type.proxy.original.UserVo;

/**
 * 继承代理
 * 适用范围：如果原始类没有定义接口，且不是我们开发维护的，没有办法直接修改原始类，一般采用继承的方式
 * */
public class Inherit {
    //UserControllerProxy使用举例
    UserController userController = new UserControllerInheritProxy();
}


class UserControllerInheritProxy extends UserController {
    private MetricsCollector metricsCollector;

    public UserControllerInheritProxy() {
        this.metricsCollector = new MetricsCollector();
    }

    public UserVo login(String telephone, String password) {
        long startTimestamp = System.currentTimeMillis();

        UserVo userVo = super.login(telephone, password);

        long endTimeStamp = System.currentTimeMillis();
        long responseTime = endTimeStamp - startTimestamp;
        RequestInfoHelper requestInfo = new RequestInfoHelper("login", responseTime, startTimestamp);
        metricsCollector.recordRequest(requestInfo);

        return userVo;
    }

    public UserVo register(String telephone, String password) {
        long startTimestamp = System.currentTimeMillis();

        UserVo userVo = super.register(telephone, password);

        long endTimeStamp = System.currentTimeMillis();
        long responseTime = endTimeStamp - startTimestamp;
        RequestInfoHelper requestInfo = new RequestInfoHelper("register", responseTime, startTimestamp);
        metricsCollector.recordRequest(requestInfo);

        return userVo;
    }
}

