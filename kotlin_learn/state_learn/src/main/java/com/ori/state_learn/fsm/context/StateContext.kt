package com.ori.state_learn.fsm.context

import com.ori.state_learn.fsm.model.BaseEvent
import com.ori.state_learn.fsm.model.BaseState
import com.ori.state_learn.fsm.transition.Transition

interface StateContext {
    fun getEvent(): BaseEvent

    fun getSource(): BaseState

    fun getTarget(): BaseState

    fun getTransition(): Transition

    fun getException(): Exception?

    fun setException(exception: Exception)
}