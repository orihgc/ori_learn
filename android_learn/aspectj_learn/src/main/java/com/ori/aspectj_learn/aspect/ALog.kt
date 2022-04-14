package com.ori.aspectj_learn.aspect

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
annotation class ALog(val value: String)
