package com.ori.state_learn.fsm

import com.ori.state_learn.fsm.model.BaseEvent
import com.ori.state_learn.fsm.state.State
import java.util.concurrent.atomic.AtomicBoolean

class StateMachine(private val initialState: State) {

    companion object {
        fun buildStateMachine(
            initialState: State,
            init: StateMachine.() -> Unit
        ): StateMachine {
            return StateMachine(initialState).apply(init)
        }
    }

    private lateinit var currentState: State
    private val states = mutableListOf<State>()
    private val initialized = AtomicBoolean(false)

    fun init() {
        if (initialized.compareAndSet(false, true)) {
            currentState = getState(initialState)
            currentState.enter()
        }
    }

    fun addState(state: State, init: State.() -> Unit): StateMachine {
        states.add(state.apply(init))
        return this
    }

    fun getState(stateType: State): State =
        states.firstOrNull { stateType.javaClass == it.javaClass }
            ?: throw NoSuchElementException("$stateType is not in state machine")

    private fun isCurrentStateInitialized() = ::currentState.isInitialized

    fun sendEvent(event: BaseEvent) {
        val transition = currentState.getTransitionWithEvent(event)
        transition.transit()
        val guard = transition.guard?.invoke() ?: true

        if (guard) {
            val state = getState(transition.targetState)
            state.enter()
            currentState = state
        }
    }

    fun getCurrentState(): State = this.currentState


}