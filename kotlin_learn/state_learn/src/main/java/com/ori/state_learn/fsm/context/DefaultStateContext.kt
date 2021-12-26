package com.ori.state_learn.fsm.context

import com.ori.state_learn.fsm.model.BaseEvent
import com.ori.state_learn.fsm.model.BaseState
import com.ori.state_learn.fsm.transition.Transition

class DefaultStateContext(
    private val event: BaseEvent,
    private val transition: Transition,
    private val sourceState: BaseState,
    private val targetState: BaseState
) :
    StateContext {

    var e: Exception? = null

    override fun getEvent(): BaseEvent = event

    override fun getSource(): BaseState = sourceState

    override fun getTarget(): BaseState = targetState

    override fun getTransition(): Transition = transition

    override fun getException(): Exception? = e

    override fun setException(exception: Exception) {
        e = exception
    }
}