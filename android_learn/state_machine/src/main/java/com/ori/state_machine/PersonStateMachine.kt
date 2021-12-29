import android.os.Message
import android.util.Log
import com.ori.state_machine.hsm.State
import com.ori.state_machine.hsm.StateMachine


class PersonStateMachine internal constructor(name: String?) : StateMachine(name) {
    //创建状态
    private val mBoringState: State = BoringState() // 默认状态
    private val mWorkState: State = WorkState() // 工作
    private val mEatState: State = EatState() // 吃
    private val mSleepState: State = SleepState() // 睡

    val lock = Object()

    override fun onHalting() {
        Log.e(TAG, "PersonStateMachine  halting")
        synchronized(lock) {
            lock.notifyAll()
        }
    }

    /**
     * 定义状态:无聊
     */
    internal inner class BoringState : State() {
        override fun enter() {
            Log.e(TAG, "BoringState  enter Boring")
        }

        override fun exit() {
            Log.e(TAG, "BoringState  exit Boring")
        }

        override fun processMessage(msg: Message?): Boolean {
            Log.e(TAG, "BoringState  processMessage.....")
            return true
        }
    }

    /**
     * 定义状态:睡觉
     */
    internal inner class SleepState : State() {
        override fun enter() {
            Log.e(TAG, "SleepState  enter Sleep")
        }

        override fun exit() {
            Log.e(TAG, "SleepState  exit Sleep")
        }

        override fun processMessage(msg: Message): Boolean {
            Log.e(TAG, "SleepState  processMessage.....")
            when (msg.what) {
                MSG_WAKEUP -> {
                    Log.e(TAG, "SleepState  MSG_WAKEUP")
                    // 进入工作状态
                    deferMessage(msg)
                    transitionTo(mWorkState)
                    //
                    //发送饿了信号...
                    sendMessage(obtainMessage(MSG_HUNGRY))
                }
                MSG_HALTING -> {
                    Log.e(TAG, "SleepState  MSG_HALTING")

                    // 停止
                    transitionToHaltingState()
                }
                else -> return false
            }
            return true
        }
    }

    /**
     * 定义状态:工作
     */
    internal inner class WorkState : State() {
        override fun enter() {
            Log.e(TAG, "WorkState  enter Work")
        }

        override fun exit() {
            Log.e(TAG, "WorkState  exit Work")
        }

        override fun processMessage(msg: Message): Boolean {
            Log.e(TAG, "WorkState  processMessage.....")
            when (msg.what) {
                MSG_HUNGRY -> {
                    Log.e(TAG, "WorkState  MSG_HUNGRY")
                    // 吃饭状态
                    deferMessage(msg)
                    transitionTo(mEatState)

                    // 发送累了信号...
                    sendMessage(obtainMessage(MSG_TIRED))
                }
                else -> return false
            }
            return true
        }
    }

    /**
     * 定义状态:吃
     */
    internal inner class EatState : State() {
        override fun enter() {
            Log.e(TAG, "EatState  enter Eat")
        }

        override fun exit() {
            Log.e(TAG, "EatState  exit Eat")
        }

        override fun processMessage(msg: Message): Boolean {
            Log.e(TAG, "EatState  processMessage.....")
            when (msg.what) {
                MSG_TIRED -> {
                    Log.e(TAG, "EatState  MSG_TIRED")
                    // 睡觉
                    deferMessage(msg)
                    transitionTo(mSleepState)

                    // 发出结束信号...
                    sendMessage(obtainMessage(MSG_HALTING))
                }
                else -> return false
            }
            return true
        }
    }

    companion object {
        private const val TAG = "MachineTest"

        //设置状态改变标志常量
        const val MSG_WAKEUP = 1 // 醒
        const val MSG_TIRED = 2 // 困
        const val MSG_HUNGRY = 3 // 饿
        private const val MSG_HALTING = 4 //停

        /**
         * @return 创建启动person 状态机
         */
        fun makePerson(): PersonStateMachine {
            Log.e(TAG, "PersonStateMachine  makePerson")
            val person = PersonStateMachine("Person")
            person.start()
            return person
        }
    }

    /**
     * 构造方法
     *
     * @param name
     */
    init {
        Log.e(TAG, "PersonStateMachine")
        //加入状态，初始化状态
        addState(mBoringState)
        addState(mSleepState, mBoringState)
        addState(mWorkState, mBoringState)
        addState(mEatState, mBoringState)

        // sleep状态为初始状态
        setInitialState(mSleepState)
    }
}