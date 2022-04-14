package com.ori.aspectj_learn.aspect

import android.util.Log
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut

@Aspect
class LogAspect {

    //定义入口
    //@注解 访问权限 返回值类型 类名.函数名
    @Pointcut("execution(@com.ori.aspectj_learn.aspect.ALog * *(..))")
    public fun log() {

    }

    @Around("log()")
    public fun aroundJoinPoint(joinPoint: ProceedingJoinPoint?) {
        //实现功能
        Log.i("MainActivity", "aLog.value")
        joinPoint?.proceed()
//        val context = obj as? Context
//        Toast.makeText(context, "aspect", Toast.LENGTH_LONG).show()
//        joinPoint.proceed()
    }

}