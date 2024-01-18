package learn.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    val mutableSharedFlow = MutableSharedFlow<String>()
    CoroutineScope(Dispatchers.IO).launch {
        mutableSharedFlow.collect {
                println("Coroutine1 $it")
        }
    }

    CoroutineScope(Dispatchers.IO).launch {
        mutableSharedFlow.collect {
                println("Coroutine2 $it")
        }
    }

    CoroutineScope(Dispatchers.IO).launch {
        mutableSharedFlow.collect {
            println("Coroutine3 $it")
        }
    }

    runBlocking {
        launch(Dispatchers.IO) {
            for (i in 0 until 50) {
                mutableSharedFlow.emit("data$i")
                delay(50)
            }
        }
    }

}

