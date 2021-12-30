package com.ori.state_learn.hsm

open class State {

    private var level = 0
    private val childStateList = mutableListOf<State>()
    private val map = hashMapOf<Event, Transition>()
    private var stateAction: (() -> Unit)? = null
    private var parentState: State? = null

    fun addChildState(state: State, initChildState: (State.() -> Unit)? = null) {
        childStateList.add(state.apply {
            this.level += this@State.level + 1
            this.parentState = this@State
        }.apply { if (initChildState != null) this.initChildState() })
    }

    fun setStateAction(stateAction: () -> Unit) {
        this.stateAction = stateAction
    }

    fun enter() {
        println("entering ${this.javaClass.simpleName}")
        stateAction?.invoke()
    }

    fun addTransition(
        event: Event,
        targetState: State,
        init: (Transition.() -> Unit)? = null
    ): State {
        if (childStateList.firstOrNull { it.javaClass == targetState.javaClass } == null
            && parentState?.childStateList?.firstOrNull { it.javaClass == targetState.javaClass } == null
            && parentState?.javaClass != targetState.javaClass
            && level != targetState.level
        ) throw StateMachineException("can not access target ")
        val transition = Transition(event, targetState)
        if (init != null) {
            transition.init()
        }

        if (map.containsKey(event)) {
            throw StateMachineException("Adding multiple transitions for the same event is invalid")
        }

        map[event] = transition
        return this
    }

    fun getTransitionByEvent(event: Event): Transition? = map[event] ?: parentState?.getTransitionByEvent(event)


}