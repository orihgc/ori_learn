import com.ori.coroutines_base_learn.core.dispatcher.Dispatchers
import com.ori.coroutines_base_learn.launch

fun main() {
    launch(Dispatchers.Default) {
        println("${Thread.currentThread().name}")
    }
    Thread.sleep(6000)
}