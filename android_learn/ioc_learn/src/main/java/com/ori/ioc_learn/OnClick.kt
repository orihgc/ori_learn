package com.ori.ioc_learn


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class OnClick(val value: Int)
