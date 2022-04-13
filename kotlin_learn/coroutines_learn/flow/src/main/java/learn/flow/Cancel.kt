package learn.flow

import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking


fun testAsFlowCancel() {
    runBlocking {
        (1..3).asFlow().collect {
            if (it == 2) {
                cancel()
            }
            println("collect $it")
        }
    }
}

fun testFlowCancel() {
    runBlocking {
        flow {
            (1..3).forEach {
                emit(it)
            }
        }.collect {
            if (it == 2) {
                cancel()
            }
            println("collect $it")
        }
    }

}

fun testCancelableFlow(){
    runBlocking {
        (1..3).asFlow().cancellable().collect {
            if (it == 2) {
                cancel()
            }
            println("collect $it")
        }
    }
}


fun main() {
    testCancelableFlow()
}