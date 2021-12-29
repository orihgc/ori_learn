package com.ori.state_learn.fsm.model


abstract class BaseEvent : Base()

abstract class Base {

    override fun equals(other: Any?): Boolean {
        val e = other as Base
        return this.javaClass == e.javaClass
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String = this.javaClass.simpleName
}