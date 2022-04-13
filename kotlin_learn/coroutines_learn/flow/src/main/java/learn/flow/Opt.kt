package learn.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.io.IOException
import kotlin.coroutines.CoroutineContext

/**
 * withIndex
 * 将上游流包装成带index的对象流，从0开始
 * */
fun testWithIndex() {
    runBlocking {
        flowOf("a", "b", "c").withIndex().collect {
            println(it.index)
            println(it.value)
        }
    }
}

/**
 * take
 * 限长操作符，取发射的前count个元素，当第count个元素被消费后，cancel掉flow
 * */
fun testTake() {
    runBlocking {
        flowOf(1, 2, 3).take(2).collect {
            println(it)
        }
    }
}

//region collect
fun testCollectIndexed() {
    runBlocking {
        flowOf("a", "b", "c").collectIndexed { index, value ->
            println(index)
            println(value)
        }
    }
}

/**
 * collectLatest
 * 在收集到发射的新值时会取消当前操作，解决生产者速率大于消费者速率的背压问题
 * */
fun testCollectLatest() {
    runBlocking {
        flowOf(1, 2, 3).collectLatest {
            delay(1)
            println(it)
        }
    }
}
//endregion

/**
 * launchIn
 * 指定作用域
 * */
fun testLaunchIn() {
    flowOf(1, 2, 3).onEach {
        println(it)
    }.launchIn(GlobalScope)
}

//region first latest single
/**
 * first last 返回发出的第一个/最后一个值，然后取消flow。如果为空，抛异常
 * firstOrNull,lastOrNull 为空，不抛异常
 * */
fun testFirst() {
    runBlocking {
        emptyFlow<String>().firstOrNull()
    }

}

/**
 * single/SingleOrNull
 * */
fun testSingle() {
    runBlocking {
        val re = flow {
            emit(1)
            emit(2)
        }.single()
        println(re)
    }
}
//endregion

//region fold reduce迭代计算
fun testFold() {
    runBlocking {
        val fold = flowOf(1, 2, 3).fold(2) { result, value ->
            result + value
        }
        println(fold)
    }
}

/**
 * reduce
 * 类似于fold，但无初始值
 * */
fun testReduce() {
    runBlocking {
        val re = flowOf(1, 2, 3).reduce { accumulator, value ->
            accumulator + value
        }
        println(re)
    }
}

//endregion

//region 背压
/**
 * buffer
 * 上游flow通过一个特定容量的channel发射，并在另一个协程里收集
 * 通过capacity指定容量
 * onBufferOverFlow溢出策略
 * */
fun testBuffer() {
    runBlocking {
        (1..100).asFlow().onStart {
            println("onStart")
        }.map {
            it
        }.buffer(3, BufferOverflow.DROP_OLDEST).onEach {
            delay(200)
            println("onEach $it")
        }.collect()
    }
}

/**
 * conflate
 * 跳过中间发射的值，只取最新的值
 * 本质上就是capacity为0，溢出策略为DROP_OLDEST的buffer操作
 * */
fun testConflate() {
    runBlocking {
        (1..100).asFlow().onStart {
            println("onStart")
        }.map {
            it
        }.conflate().onEach {
            delay(200)
            println("onEach $it")
        }.collect()
    }
}
//endregion

//region retry

fun testRetry() {
    runBlocking {
        flow {
            emit(1)
            throw IOException("IO")
        }.retry(3).collect {
            println("collect: $it")
        }
    }
}

fun testRetryWhen() {
    runBlocking {
        flow<Int> {
            emit(1)
            throw IOException("IO")
        }.retryWhen { cause, attempt ->
            if (attempt > 3) {
                return@retryWhen false
            }
            true
        }.collect {
            println("collect: $it")
        }
    }
}
//endregion

/**
 * debounce
 * 把发射时间距离前一个发射时间小于timeout的值过滤掉，但最后一个值必然会被发射
 * */
fun testDebounce() {
    runBlocking {
        flowOf(1, 2, 3, 4).withIndex()
            .onEach { delay(101L * it.index) }.debounce(300).collect {
                println(it)
            }
    }
}

fun main() {
    testDebounce()
}

