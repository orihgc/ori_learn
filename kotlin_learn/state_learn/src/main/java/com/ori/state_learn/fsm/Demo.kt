package com.ori.state_learn.fsm

import com.ori.state_learn.fsm.model.BaseEvent
import com.ori.state_learn.fsm.state.State
import kotlin.concurrent.thread

class Cook : BaseEvent()      // 烧菜
class WashDishes : BaseEvent() // 洗碗

class Initial : State()   // 初始化状态
class Eat : State()       // 吃饭状态
class WatchTV : State()   // 看电视状态

fun main() {
    val state = Initial()
    val stateMachine = StateMachine.buildStateMachine(Initial()) {
        addState(state) {
            action { println("Entered [$it] State") }
            transition(Cook(), Eat()) {
                action {
                    println("Action:Wash Vegetables")
                }
                action {
                    println("Action:Cook")
                }
            }
        }

        addState(Eat()) {
            action {
                println("Entered[$it] State")
            }
            transition(WashDishes(), WatchTV()) {
                action {
                    println("Action:Wash Dishes")
                }
                action {
                    println("Action: Turn on the TV")
                }
            }
        }

        addState(WatchTV()) {
            action {
                println("Entered [$it] State")
            }
        }
    }

    stateMachine.init()
    stateMachine.sendEvent(Cook())
    stateMachine.sendEvent(WashDishes())
    thread {
        stateMachine.sendEvent(Cook())
    }
    stateMachine.getState(state)
    println()
}