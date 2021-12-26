package com.ori.state_learn.fsm

import com.ori.state_learn.fsm.model.BaseEvent
import com.ori.state_learn.fsm.model.BaseState

class Cook : BaseEvent()      // 烧菜
class WashDishes : BaseEvent() // 洗碗

class Initial : BaseState()   // 初始化状态
class Eat : BaseState()       // 吃饭状态
class WatchTV : BaseState()   // 看电视状态

fun main() {
    val stateMachine = StateMachine.buildStateMachine(Initial()) {
        state(Initial()) {
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

        state(Eat()) {
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

        state(WatchTV()) {
            action {
                println("Entered [$it] State")
            }
        }
    }

    stateMachine.init()
    stateMachine.sendEvent(Cook())
    stateMachine.sendEvent(WashDishes())
}