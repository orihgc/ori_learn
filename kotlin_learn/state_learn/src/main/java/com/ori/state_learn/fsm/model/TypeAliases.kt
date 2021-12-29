package com.ori.state_learn.fsm.model

import com.ori.state_learn.fsm.state.State
import com.ori.state_learn.fsm.transition.Transition


typealias StateAction = (State) -> Unit

typealias TransitionAction = (Transition) -> Unit

typealias Guard = () -> Boolean