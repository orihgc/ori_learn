package com.ori.state_learn.fsm

import com.ori.state_learn.fsm.context.DefaultStateContext
import com.ori.state_learn.fsm.model.BaseEvent
import com.ori.state_learn.fsm.model.BaseState
import com.ori.state_learn.fsm.state.State
import java.util.concurrent.atomic.AtomicBoolean

class StateMachine(private val initialState: BaseState) {

    companion object {
        fun buildStateMachine(
            initialStateName: BaseState,
            init: StateMachine.() -> Unit
        ): StateMachine {
            return StateMachine(initialStateName).apply(init)
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

    fun state(stateName: BaseState, init: State.() -> Unit): StateMachine =
        state(State(stateName).apply(init))

    fun state(state: State): StateMachine {
        states.add(state)
        return this
    }

    private fun getState(stateType: BaseState): State =
        states.firstOrNull { stateType.javaClass == it.name.javaClass }
            ?: throw NoSuchElementException("$stateType is not in state machine")

    private fun isCurrentStateInitialized() = ::currentState.isInitialized

    fun sendEvent(event: BaseEvent) {
        val transition = currentState.getTransitionForEvent(event)
        transition.transit()
        val guard = transition.guard?.invoke() ?: true

        if (guard) {
            val state = getState(transition.targetState)
            state.enter()
            currentState = state
        }
    }

    fun getCurrentState(): BaseState = this.currentState.name


}