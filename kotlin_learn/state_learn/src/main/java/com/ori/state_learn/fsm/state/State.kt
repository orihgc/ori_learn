package com.ori.state_learn.fsm.state

import com.ori.state_learn.fsm.exception.StateMachineException
import com.ori.state_learn.fsm.model.BaseEvent
import com.ori.state_learn.fsm.model.StateAction
import com.ori.state_learn.fsm.transition.Transition
import java.lang.IllegalStateException

open class State {

    private val stateActions = mutableListOf<StateAction>()
    private val transitions = hashMapOf<BaseEvent, Transition>()

    fun transition(event: BaseEvent, targetState: State, init: Transition.() -> Unit): State {

        val transition = Transition(event, targetState)
        transition.init()

        if (transitions.containsKey(event)) {
            throw StateMachineException("Adding multiple transitions for the same event is invalid")
        }
        transitions[event] = transition
        return this
    }

    fun getTransitionWithEvent(event: BaseEvent): Transition {
        return transitions[event]
            ?: throw IllegalStateException("Event $event isn't registered with state $this")
    }


    fun action(action: StateAction) {
        stateActions.add(action)
    }

    fun enter() {
        stateActions.forEach {
            it.invoke(this)
        }
    }

    override fun toString(): String = this.javaClass.simpleName
}