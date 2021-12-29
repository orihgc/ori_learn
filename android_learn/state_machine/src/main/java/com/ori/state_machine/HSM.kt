package com.ori.state_machine

import android.os.Message
import android.util.Log
import com.ori.state_machine.hsm.State
import com.ori.state_machine.hsm.StateMachine

class HSM constructor(name: String?) : StateMachine(name) {

    private val p0 = P0()
    private val p1 = P1()
     val p2 = P2()
    private val s1 = S1()
    private val s2 = S2()

    companion object {
        const val TAG = "HSM"
        const val MSG_P0_P1 = 0
        const val MSG_P1_P2 = 1
        const val MSG_P1_S1 = 2
        const val MSG_S1_S2 = 3
    }

    init {
        addState(p0)
        addState(p1)
        addState(s1, p1)
        addState(s2, p1)
        addState(p2)
        setInitialState(p0)
        start()
    }


    internal inner class S1 : State() {
        override fun enter() {
            Log.d(TAG, "enter S1")
            deferMessage(obtainMessage(MSG_S1_S2))
        }

        override fun exit() {
            Log.d(TAG, "exit S1")
        }


        override fun processMessage(msg: Message?): Boolean {
            return when (msg?.what) {
                MSG_S1_S2 -> {
                    transitionTo(s2)
                    true
                }
                else -> false
            }
        }
    }

    internal inner class S2 : State() {
        override fun enter() {
            Log.d(TAG, "enter S2")
        }

        override fun exit() {
            Log.d(TAG, "exit S2")
        }

        override fun processMessage(msg: Message?): Boolean {
            return super.processMessage(msg)
        }
    }

    internal inner class P0 : State() {
        override fun enter() {
            Log.d(TAG, "enter P0")
        }

        override fun exit() {
            Log.d(TAG, "exit P0")
        }


        override fun processMessage(msg: Message?): Boolean {
            return when (msg?.what) {
                MSG_P0_P1 -> {
                    transitionTo(p1)
                    true
                }
                else -> false
            }
        }
    }

    internal inner class P1 : State() {
        override fun enter() {
            Log.d(TAG, "enter P1")
            deferMessage(obtainMessage(MSG_P1_S1))
        }

        override fun exit() {
            Log.d(TAG, "exit P1")
        }

        override fun processMessage(msg: Message?): Boolean {
            return when (msg?.what) {
                MSG_P1_P2 -> {
                    transitionTo(p2)
                    true
                }
                MSG_P1_S1 -> {
                    transitionTo(s1)
                    true
                }
                else -> false
            }
        }
    }

    inner class P2 : State() {

        override fun enter() {
            Log.d(TAG, "enter P2")
        }

        override fun exit() {
            Log.d(TAG, "exit P2")
        }

        override fun processMessage(msg: Message?): Boolean {
            return false
        }
    }
}