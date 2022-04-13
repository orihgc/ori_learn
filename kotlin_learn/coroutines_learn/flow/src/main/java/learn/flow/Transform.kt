package learn.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

//region transform
/**
 * transform
 * 用于上游发射值的转换
 * */
fun testTransform() {
    runBlocking {
        flowOf(1, 2, 3).transform {
            emit(it * 2)
        }.collect {
            println(it)
        }
    }
}


/**
 * transformLatest
 * 一旦上游发射新值，会取消当前正在执行的transform闭包
 * */
fun testTransformLatest() {
    runBlocking {
        flow {
            emit("a")
            delay(100)
            emit("b")
        }.transformLatest {
            emit(it)
            delay(200)
            emit(it + "_latest")
        }.collect {
            println(it)
        }
    }
}

/**
 * transformWhile
 * 返回值为false时，会cancel
 * */
fun testTransformWhile() {
    runBlocking {
        (1..10).asFlow()
            .transformWhile {
                emit(it)
                emit("$it/2")
                it <= 2
            }.collect {
                println(it)
            }
    }
}
//endregion

//region map
/**
 * map
 * 原理和transform相同，省去了再闭包中emit
 * */
fun testMap() {
    runBlocking {
        flowOf(1, 2, 3).map {
            it * 2
        }.collect {
            println(it)
        }
    }
}

fun testMapNotNull() {
    runBlocking {
        flowOf("a", null).mapNotNull {
            it
        }.collect {
            println(it)
        }
    }
}

/**
 * mapLatest
 * 同transformLatest，新发送的值会取消mapLatest中正在执行的动作以实现防抖
 * */
fun testMapLatest() {

}
//endregion

//region filter
/**
 * filter 原理还是transform
 * false过滤不符合预期的值
 */
fun testFilter() {
    runBlocking {
        flowOf(1, 2, 3).filter {
            it > 2
        }.collect {
            println(it)
        }
    }
}

/**
 * filterNot
 * 过滤不符合预期的值
 * */

/**
 * filterNotNull
 * 过滤掉所有发射的null值，保证流的非空
 * */
//endregion


fun main() {
    testFilter()
}