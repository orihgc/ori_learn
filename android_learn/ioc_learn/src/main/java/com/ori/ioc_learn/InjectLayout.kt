package com.ori.ioc_learn

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class InjectLayout(val value: Int = 0)
