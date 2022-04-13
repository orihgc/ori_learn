package com.ori.ioc_learn


@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class InjectView(val value: Int = 0)
