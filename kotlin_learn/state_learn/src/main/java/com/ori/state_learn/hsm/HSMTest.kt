package com.ori.state_learn.hsm

object S1 : State()
object S2 : State()
object S3 : State()
object S4 : State()
object S5 : State()
object S6 : State()
object S7 : State()
object S8 : State()

object S1ToS2 : Event()
object S2ToS3 : Event()
object S2ToS4 : Event()
object S2ToS1 : Event()
object S1ToS6 : Event()
object S6ToS7 : Event()
object S4ToS8 : Event()


fun main() {
    val hStateMachine = HStateMachine(S1)
    hStateMachine.addState(S1) {
        setStateAction {
            println("state action S1")
        }
        addChildState(S3)
        addChildState(S2) {
            setStateAction { println("state action S2") }
            addChildState(S4) {
                setStateAction { println("state action S4") }
                addChildState(S8) {
                    setStateAction { println("state action S8") }
                }
                addTransition(S4ToS8, S8) {}
            }
            addTransition(S2ToS3, S3)
            addTransition(S2ToS4, S4)
            addTransition(S2ToS1, S1)
        }
        addTransition(S1ToS2, S2) {
            guard = {
                false
            }
            blockedAction = {
                println("S1ToS2 guard blocked")
            }
        }
        addTransition(S1ToS6, S6)
    }
    hStateMachine.addState(S6) {
        setStateAction { println("state action S6") }
        addChildState(S7) {
            setStateAction { println("state action S7") }
        }
        addTransition(S6ToS7, S7) {}
    }
    hStateMachine.initStateMachine()
    hStateMachine.processEvent(S1ToS2)
    hStateMachine.processEvent(S2ToS4)
    hStateMachine.processEvent(S4ToS8)
    hStateMachine.processEvent(S2ToS3)
    hStateMachine.processEvent(S1ToS6)
    hStateMachine.processEvent(S6ToS7)
}