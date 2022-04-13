package learn.flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.math.BigInteger

//region flow特性
suspend fun fetchUserName(userId: Int): String {
    delay(2000)
    return "user$userId"
}

suspend fun filterInvalidName(name: String): Boolean {
    delay(1000)
    return name != "user1"
}

/**
 * 流是冷的，当没有消费者时不会产生数据也不会执行中间操作符的方法
 * 操作符传入的transform function是suspend方法，可以挂起
 *
 * 顺序消费，默认在同一个线程挂起等待
 * */
fun testFlow() {
    val testFlow = flowOf(1, 2, 3).map {
        fetchUserName(it)
    }.filter {
        filterInvalidName(it)
    }
    runBlocking {
        testFlow.onStart {
            println("start")
        }.collect {
            println("collect: $it")
        }
    }
}
//endregion

//region 流是冷的
fun fibonacci(): Flow<BigInteger> = flow {
    var x = BigInteger.ZERO
    var y = BigInteger.ONE
    while (true) {
        emit(x)
        x = y.also {
            y += x
        }
    }
}

fun testFibonacci() {
    runBlocking {
        fibonacci().take(100).collect { println(it) }
        println(fibonacci().take(100).toList().joinToString(","))
    }
}
//endregion

//region 创建flow
/**
 * flow默认生产者和消费者之间是同步非阻塞的，生产者和消费者是在同一个协程或线程
 * */
fun createFlow() {
    runBlocking {
        flow {
            delay(1000)
            emit(1)
            withContext(Dispatchers.IO) {
                emit(2)
            }
        }.catch {
            println("不要在flow中切换协程上下文")
        }.collect { println(it) }
    }
}
//endregion

//region channelFlow
/**
 * channelFlow支持生产者和消费者之间的异步非阻塞的
 * */
fun testChannelFlow() {
    runBlocking {
        val channelFlow = channelFlow {
            (1..3).forEach {
                delay(1000)
                println("send $it")
                send(it)
            }
        }
        delay(2000)
        channelFlow.onStart {
            println("testChannelFlow start")
        }.collect {
            delay(2000)
            println(it)
        }
    }
}

/**
 * 如果仅仅为了实现发送方接收方异步，指定生产者在另一个协程是否也行
 * 结果和直接使用channelFlow一样
 * */
fun testFlowSwitchContext() {
    runBlocking {
        val flowOn = flow {
            (1..3).forEach {
                delay(1000)
                println("send $it")
                emit(it)
            }
        }.flowOn(Dispatchers.IO)
        delay(2000)
        flowOn.onStart {
            println("testFlowSwitchContext start")
        }.collect {
            delay(2000)
            println(it)
        }
    }
}
//endregion

//region callbackFlow
interface MyCallBack {
    fun onSuccess(res: String)
    fun onFailure(errCode: Int)
    fun onError(e: Throwable)
    fun onCompleted()
}

fun callTranslateApi(articleList: List<String>, callBack: MyCallBack) {
    runBlocking {
        try {
            articleList.forEach {
                delay(2000)
                val translation = "=${it}="
                if (translation.length > 10) {
                    callBack.onFailure(-1)
                } else {
                    callBack.onSuccess(translation)
                }
            }
        } catch (e: Throwable) {
            callBack.onError(e)
        } finally {
            callBack.onCompleted()
        }
    }
}

//endregion

fun main() {
    testFlowSwitchContext()
}


