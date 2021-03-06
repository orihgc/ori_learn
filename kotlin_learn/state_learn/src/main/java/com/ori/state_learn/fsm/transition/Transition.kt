package com.ori.state_learn.fsm.transition

import com.ori.state_learn.fsm.model.BaseEvent
import com.ori.state_learn.fsm.model.Guard
import com.ori.state_learn.fsm.model.TransitionAction
import com.ori.state_learn.fsm.state.State

class Transition(
    val event: BaseEvent,
    val targetState: State,
) {

    private val actions = mutableListOf<TransitionAction>()
    var guard: Guard? = null

    fun transit() {
        actions.forEach {
            it.invoke(this)
        }
    }

    fun action(action: TransitionAction) {
        actions.add(action)
    }

//    fun applyTransition(getNextState: (BaseState) -> State): State = getNextState(targetState)


}