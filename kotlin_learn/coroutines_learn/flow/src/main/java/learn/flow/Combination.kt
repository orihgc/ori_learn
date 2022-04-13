package learn.flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/**
 * zip
 * 组合两个流中发射的值，组合后，流会在其中一个流结束时结束
 * zip适用于两个流严格一一对应组合的情况，任何流发射的任何值都只会消费一次
 * 只有两个流都发射值时才会触发zip后新流的发射
 * 默认都在同一个协程中发射、消费数据
 * */
fun testZip() {
    runBlocking {
        val flowA = flowOf(1, 2, 3, 4, 5).withIndex().onEach {
            delay(it.index * 1000L)
            println("flowA emit:${it.value}")
        }.flowOn(Dispatchers.IO)
        val flowB = flowOf("a", "b", "c").withIndex().onEach {
            delay(3000L - it.index * 1000)
            println("flowB emit:${it.value}")
        }.flowOn(Dispatchers.IO)
        flowA.zip(flowB) { aInt, bStr ->
            "${aInt.value} with ${bStr.value}"
        }.collect {
            println("collect: $it")
        }
    }
}

/**
 * combine
 * 结合两个流成为一个新流，两个流的任意一个流发射值都会结合另一个流当前的最新值执行传入transform方法并将返回值发送至新流
 * combine即使不指定协程上下文也会异步发射
 * */
fun testCombine() {
    runBlocking {
        val flowA = flowOf(1, 2, 3, 4, 5).withIndex().onEach {
            delay(it.index * 100L)
            println("flowA emit:${it.value}")
        }.flowOn(Dispatchers.IO)
        val flowB = flowOf("a", "b", "c").withIndex().onEach {
            delay(300L - it.index * 100)
            println("flowB emit:${it.value}")
        }.flowOn(Dispatchers.IO)
        flowA.combine(flowB) { aInt, bStr ->
            "${aInt.value} with ${bStr.value}"
        }.collect {
            println("collect: $it")
        }
    }
}

/**
 * merge
 * 合并两个或多个流，按照每个流各自的时间顺序向新流中发射，合并后的新流是一个channelFlow，生成消费按照异步非阻塞模型
 * 1、merge后的流收集在单独的协程中
 * 2、被merge的流独立发射，互不影响
 * */
fun testMerge() {
    runBlocking {
        val flowA = flowOf(1, 2, 3).onEach {
            delay(1000)
            println("flowA emit:${it}")
        }
        val flowB = flowOf("a", "b", "c").onEach {
            delay(2000L)
            println("flowB emit:${it}")
        }

        listOf(flowA, flowB).merge().collect {
            println("collect: $it")

        }
    }
}

//region flatten
fun testFlattenConcat() {
    runBlocking {
        val flowA = flow {
            emit(flowOf(1, 2, 3).onEach {
                println("emit $it")
            })
            emit(flowOf(4,5,6).onEach {
                println("emit $it")
            })
        }
        flowA.flattenConcat().collect {
            println(it)
        }
    }
}

/**
 * flattenMerge
 * 展开流，同merge可以并发收集流，并且产生一个channelFlow
 * 入参concurrency用于控制可以并发收集的流的个数
 * */
fun testFlattenMerge(){
    runBlocking {
        val flowA = flow {
            emit(flowOf(1, 2, 3).onEach {
                delay(1000)
                println("emit $it")
            })
            emit(flowOf(4,5,6).onEach {
                delay(1000)
                println("emit $it")
            })
        }
        flowA.flattenMerge(2).collect {
            println("collect $it")
        }
    }
}
//endregion

//region flat
/**
 * flatMapConcat
 * 先通过map转换成Flow<Flow<T>>,再通过flttenConcat展开
 * */
fun testFlatMapConcat(){
    runBlocking {
        flowOf(1,2,3).flatMapConcat {
            (0..it).asFlow()
        }.collect {
            println("collect $it")
        }
    }
}

/**
 * flatMapMerge
 * */
//endregion

fun main() {

}