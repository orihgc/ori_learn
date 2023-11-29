package test.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    var count = 0 //重试计数

    runBlocking {
        load().onEach {
            if (it == null) {
                throw NullPointerException()
            } else {
                println("获取数据 $it")
            }
        }.retry(3) { //重试3次
            println("retry ${count++}")

            delay(1000) //延时重试

            it is NullPointerException
        }.catch {
            it.printStackTrace() //如果重试3次仍失败，此次将抛出异常打印错误栈
        }.onCompletion { println("onCompletion") }
            .collect { println("collect $it") }
    }
}

fun load() = flow {
    //生成一个概率布尔，模拟加载概率性成功与失败
    val b = Boolean.let {
        val p: Int = (Math.random() * 10).toInt() % 2
        when (p) {
            0 -> true
            1 -> false
            else -> false
        }
    }

    var s: String? = null
    if (b) {
        println("加载成功")
        s = "fly"
    } else {
        println("加载失败")
    }

    emit(s)
}