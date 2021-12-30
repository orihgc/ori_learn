package com.ori.state_learn.hsm

import java.util.concurrent.atomic.AtomicBoolean


class HStateMachine(private val initialState: State) {

    private lateinit var currentState: State
    private val states = mutableListOf<State>()
    private val initialized = AtomicBoolean(false)

    fun initStateMachine() {
        if (initialized.compareAndSet(false, true)) {
            currentState = getState(initialState)
            currentState.enter()
        }
    }

    private fun getState(state: State) =
        states.firstOrNull { it.javaClass == state.javaClass }
            ?: throw NoSuchElementException("$state is not in state machine")

    fun addState(state: State, initState: State.() -> Unit) {
        states.add(state.apply(initState))
    }

    fun processEvent(event: Event) {
        val transition = currentState.getTransitionByEvent(event) ?: return
        val guard = transition.guard?.invoke() ?: true
        if (guard) {
            transition.transitionAction?.invoke()
            transition.targetState.enter()
            currentState = transition.targetState
        } else {
            transition.blockedAction?.invoke()
        }
    }


}