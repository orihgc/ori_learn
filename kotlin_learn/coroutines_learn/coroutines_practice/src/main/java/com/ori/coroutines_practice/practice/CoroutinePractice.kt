package com.ori.coroutines_practice.practice

import android.util.Log
import kotlinx.coroutines.*

class CoroutinePractice {
    /**
     * 运行结果:
     *   runBlocking: 启动一个协程
     *   runBlockingJob: 41
     *   launch: 启动一个协程
     *   launchJob: StandaloneCoroutine{Active}@b94e973
     *   async: 启动一个协程
     *   asyncJob: DeferredCoroutine{Active}@f7aa030
     *
     * */
    private fun start() {
        /**
         * runBlocking主要是将常规的阻塞代码连接到一起，主要用于mian函数和测试中
         * */
        val runBlockingJob = runBlocking {
            Log.d("runBlocking", "启动一个协程")
        }
        Log.d("runBlockingJob", "$runBlockingJob")
        /**
         * launch最终返回一个StandaloneCoroutine，它继承自一个Job
         * */
        val launchJob = GlobalScope.launch {
            Log.d("launch", "启动一个协程")
        }
        Log.d("launchJob", "$launchJob")
        /**
         * async返回的是一个DeferredCoroutine，它继承自一个Deferred，一个携带返回值的Job
         * */
        val asyncJob = GlobalScope.async {
            Log.d("async", "启动一个协程")
            "我是返回值"
        }
        Log.d("asyncJob", "$asyncJob")
    }

    /**
     * 调用launch必须要在协程作用域(Coroutine Scope)中才能调用
     * await()是在不阻塞线程的情况下等待该值的完成并继续执行
     * D/launchJob: StandaloneCoroutine{Active}@f3d8da3
     * D/async: 启动一个协程
     * D/launch: 启动一个协程
     * D/asyncJob.await: :我是async返回值
     * D/asyncJob: DeferredCoroutine{Completed}@d6f28a0
     * */
    private fun start1() {
        GlobalScope.launch {
            val launchJob = launch {
                Log.d("launch", "启动一个协程")
            }
            Log.d("launchJob", "$launchJob")
            val asyncJob = async {
                Log.d("async", "启动一个协程")
                "我是async返回值"
            }
            Log.d("asyncJob.await", ":${asyncJob.await()}")
            Log.d("asyncJob", "$asyncJob")
        }
    }

    /**
     * 某个协程满足以下几点，那它里面的子协程将会是同步执行的:
     * 1、父协程的协程调度器是处于Dispatchers.Main情况下启动。
     * 2、同时子协程在不修改协程调度器下的情况下启动。
     * */
    private fun start2() {
        GlobalScope.launch(Dispatchers.Main) {
            for (index in 1 until 10) {
                //同步执行
                launch {
                    Log.d("launch$index", "启动一个协程")
                }
            }
        }
    }

    /**
     * 协程调度器
     * 定义：调度器它确定了相关的协程在哪个线程或哪些线程上执行。协程调度器可以将协程限制在一个特定的线程执行，或将它分派到一个线程池，亦或是让它不受限地运行。
     * Default：默认调度器，CPU密集型任务调度器，适合处理后台计算。
     * IO：IO调度器，IO密集型任务调度器，适合执行IO相关操作。比如：网络处理，数据库操作，文件操作等
     * Main：UI调度器， 即在主线程上执行，通常用于UI交互，刷新等
     * Unconfined：非受限调度器，又或者称为“无所谓”调度器，不要求协程执行在特定线程上
     * */
    private fun start3() {
        /**
         * 如果没有参数，实际上使用的是默认调度器
         * */
        GlobalScope.launch {
            Log.d("launch", "启动一个协程")
        }
        //等同于
        GlobalScope.launch(Dispatchers.Default) {
            Log.d("launch", "启动一个协程")
        }
        /**
         * 中途切换线程
         * 避免回调地狱
         * */
        GlobalScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                //网络请求...
                "请求结果"
            }
        }

        /**
         * 协程上下文
         * +号可以把多个Element整合到一个集合中
         * D/coroutineContext1: [JobImpl{Active}@21a6a21, CoroutineName(这是第一个上下文)]
         * D/coroutineContext2: [JobImpl{Active}@21a6a21, CoroutineName(这是第二个上下文), Dispatchers.Default]
         * D/coroutineContext3: [JobImpl{Active}@21a6a21, CoroutineName(这是第三个上下文), Dispatchers.Main]
         * */
        val coroutineContext1 = Job() + CoroutineName("这是第一个上下文")
        Log.d("coroutineContext1", "$coroutineContext1")
        val coroutineContext2 = coroutineContext1 + Dispatchers.Default + CoroutineName("这是第二个上下文")
        Log.d("coroutineContext2", "$coroutineContext2")
        val coroutineContext3 = coroutineContext2 + Dispatchers.Main + CoroutineName("这是第三个上下文")
        Log.d("coroutineContext3", "$coroutineContext3")

        /**
         * 协程启动模式
         * DEFAULT 默认启动模式，我们可以称之为饿汉启动模式，因为协程创建后立即开始调度，虽然是立即调度，单不是立即执行，有可能在执行前被取消。
         * LAZY 懒汉启动模式，启动后并不会有任何调度行为，直到我们需要它执行的时候才会产生调度。也就是说只有我们主动的调用Job的start、join或者await等函数时才会开始调度。
         * ATOMIC 一样也是在协程创建后立即开始调度，但是它和DEFAULT模式有一点不一样，通过ATOMIC模式启动的协程执行到第一个挂起点之前是不响应cancel 取消操作的，ATOMIC一定要涉及到协程挂起后cancel 取消操作的时候才有意义。
         * UNDISPATCHED 协程在这种模式下会直接开始在当前线程下执行，直到运行到第一个挂起点。这听起来有点像 ATOMIC，不同之处在于UNDISPATCHED是不经过任何调度器就开始执行的。当然遇到挂起点之后的执行，将取决于挂起点本身的逻辑和协程上下文中的调度器。
         *
         * 输出
         * D/defaultJob: CoroutineStart.DEFAULT（可能没有，该Job被取消了）
         * D/atomicJob: CoroutineStart.ATOMIC挂起前
         * D/undispatchedJob: CoroutineStart.UNDISPATCHED挂起前
         * */

        /**
         * DEFAULT模式协程创建后立即开始调度，但不立即执行，所以可能被取消
         * */
        val defaultJob = GlobalScope.launch {
            Log.d("defaultJob", "CoroutineStart.DEFAULT")
        }
        defaultJob.cancel()

        val lazyJob = GlobalScope.launch(start = CoroutineStart.LAZY) {
            Log.d("lazyJob", "CoroutineStart.LAZY")
        }

        /**
         * ATOMIC模式启动时也调用了cancel取消协程，但因为没有遇到挂起点
         * 所以挂起前的日志输出了，但挂起后的日志没有输出
         * */
        val atomicJob = GlobalScope.launch(start = CoroutineStart.ATOMIC) {
            Log.d("atomicJob", "CoroutineStart.ATOMIC挂起前")
            delay(100)
            Log.d("atomicJob", "CoroutineStart.ATOMIC挂起后")
        }
        atomicJob.cancel()

        /**
         * UNDISPATCHED模式启动的时候也接着调用了cancel取消协程，同样的因为没有遇到挂起点所以输出了UNDISPATCHED挂起前，但是因为UNDISPATCHED是立即执行的，所以他的日志UNDISPATCHED挂起前输出在ATOMIC挂起前的前面。
         * */
        val undispatchedJob = GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            Log.d("undispatchedJob", "CoroutineStart.UNDISPATCHED挂起前")
            delay(100)
            Log.d("atomicJob", "CoroutineStart.UNDISPATCHED挂起后")
        }
        undispatchedJob.cancel()

        /**
         * 协程作用域
         * 协程作用域CoroutineScope为协程定义作用范围，每个协程生成器launch、async等都是CoroutineScope的扩展，并继承了它的coroutineContext自动传播其所有Element和取消。协程作用域本质是一个接口,不建议手工实现该接口，而应该首选委托实现
         * 分类：
         * 顶级作用域 --> 没有父协程的协程所在的作用域称之为顶级作用域。
         * 协同作用域 --> 在协程中启动一个协程，新协程为所在协程的子协程。子协程所在的作用域默认为协同作用域。此时子协程抛出未捕获的异常时，会将异常传递给父协程处理，如果父协程被取消，则所有子协程同时也会被取消。
         * 主从作用域 --> 官方称之为监督作用域。与协同作用域一致，区别在于该作用域下的协程取消操作的单向传播性，子协程的异常不会导致其它子协程取消。但是如果父协程被取消，则所有子协程同时也会被取消。
         * */

        /**
         * 子协程会继承父协程上下文中的Element
         * 如果自己有相同key的成员，则覆盖对应的key
         * */
        GlobalScope.launch(Dispatchers.Main) {
            Log.d("父协程上下文", "$coroutineContext")
            launch(CoroutineName("第一个子协程")) {
                Log.d("第一个子协程上下文", "$coroutineContext")
            }
            launch(Dispatchers.Unconfined) {
                Log.d("第二个子协程协程上下文", "$coroutineContext")
            }
        }

        /**
         * 协同作用域
         * 如果子协程抛出未捕获的异常，会将异常传递给父协程处理
         * 如果父协程被取消，则所有子协程同时也会被取消
         * D/scope: --------- 1
         * D/scope: --------- 2
         * D/exceptionHandler: CoroutineName(scope1) java.lang.NullPointerException: 空指针
         * */
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d("exceptionHandler", "${coroutineContext[CoroutineName]} $throwable")
        }
        GlobalScope.launch(Dispatchers.Main + CoroutineName("scope1") + exceptionHandler) {
            Log.d("scope", "--------- 1")
            /**
             * scpoe2抛出一个异常，将异常传递给父协程处理，但任意一个子协程异常退出会导致整体退出
             * 导致父协程scope1未执行完成就取消了，同时还未执行完子协程scope3也取消了
             * */
            launch(CoroutineName("scope2") + exceptionHandler) {
                Log.d("scope", "--------- 2")
                throw  NullPointerException("空指针")
                Log.d("scope", "--------- 3")
            }
            val scope3 = launch(CoroutineName("scope3") + exceptionHandler) {
                Log.d("scope", "--------- 4")
                delay(2000)
                Log.d("scope", "--------- 5")
            }
            scope3.join()
            Log.d("scope", "--------- 6")
        }

        /**
         * 主从监督作用域
         * 该作用域下的协程取消操作的单向传播性，子协程的异常不会导致其他子协程取消
         * D/scope: --------- 1
         * D/scope: --------- 2
         * D/exceptionHandler: CoroutineName(scope2) java.lang.NullPointerException: 空指针
         * D/scope: --------- 6
         * D/scope: --------- 7
         * D/scope: --------- 8
         * */
        GlobalScope.launch(Dispatchers.Main + CoroutineName("scope1") + exceptionHandler) {
            supervisorScope {
                Log.d("scope", "--------- 1")
                /**
                 * 子协程scope2抛出一个异常，将异常传递给scope1处理，因为在于主从(监督)作用域下的协程取消操作是单向传播性，因此协程scope2的异常并没有导致父协程退出
                 * */
                launch(CoroutineName("scope2")) {
                    Log.d("scope", "--------- 2")
                    throw  NullPointerException("空指针")
                    Log.d("scope", "--------- 3")
                    /**
                     * 但是会导致子协程scope3退出
                     * */
                    val scope3 = launch(CoroutineName("scope3")) {
                        Log.d("scope", "--------- 4")
                        delay(2000)
                        Log.d("scope", "--------- 5")
                    }
                    scope3.join()
                }
                val scope4 = launch(CoroutineName("scope4")) {
                    Log.d("scope", "--------- 6")
                    delay(2000)
                    Log.d("scope", "--------- 7")
                }
                scope4.join()
                Log.d("scope", "--------- 8")
            }
        }

        /**
         * 主从监督作业
         * D/scope: 1--------- CoroutineName(scope2)
         * D/exceptionHandler: CoroutineName(scope2) java.lang.NullPointerException: 空指针
         * D/scope: 2--------- CoroutineName(scope3)
         * D/scope: 4--------- CoroutineName(coroutineScope)
         * D/scope: 5--------- CoroutineName(coroutineScope)
         * D/scope: 6--------- CoroutineName(scope1)
         * */
        val coroutineScope = CoroutineScope(SupervisorJob() + CoroutineName("coroutineScope"))
        GlobalScope.launch(Dispatchers.Main + CoroutineName("scope1") + exceptionHandler) {
            with(coroutineScope) {
                /**
                 * 子协程scope2的异常没有导致coroutineScope作用域下的协程取消退出
                 * */
                val scope2 = launch(CoroutineName("scope2") + exceptionHandler) {
                    android.util.Log.d("scope", "1--------- ${coroutineContext[CoroutineName]}")
                    throw  NullPointerException("空指针")
                }

                /**
                 * 由于调用了coroutineScope.cancel()，所以没有输出3
                 * */
                val scope3 = launch(CoroutineName("scope3") + exceptionHandler) {
                    scope2.join()
                    android.util.Log.d("scope", "2--------- ${coroutineContext[CoroutineName]}")
                    delay(2000)
                    android.util.Log.d("scope", "3--------- ${coroutineContext[CoroutineName]}")
                }
                scope2.join()
                android.util.Log.d("scope", "4--------- ${coroutineContext[CoroutineName]}")
                coroutineScope.cancel()
                scope3.join()
                android.util.Log.d("scope", "5--------- ${coroutineContext[CoroutineName]}")
            }
            Log.d("scope", "6--------- ${coroutineContext[CoroutineName]}")
        }

        /**
         * 挂起函数
         * suspend关键字修饰的函数就是挂起函数，挂起函数只能在协程体内，或者在其他挂起函数内调用
         * 协程体本身就是Continuation
         * 在协程内部挂起函数的调用处就是挂起点，如果挂起点出现异步调用，那么当前协程就被挂起，直到对应的Continuation通过调用resumeWith函数才会恢复协程的执行，同时返回Result<T>类型的成功或者失败的结果。
         * */
    }
}