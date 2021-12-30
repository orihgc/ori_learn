package com.ori.state_learn.hsm

class Transition(val event: Event, val targetState: State) {
    var transitionAction: (() -> Unit)? = null
    var blockedAction: (() -> Unit)? = null
    var guard: (() -> Boolean)? = null
}