import com.ori.coroutines_base_learn.core.cancel.suspendCancellableCoroutine
import com.ori.coroutines_base_learn.core.scope.GlobalScope
import com.ori.coroutines_base_learn.core.utils.log
import com.ori.coroutines_base_learn.launch
import org.junit.Test
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random

class CancellationSample {
    @Test
    fun test() {
        val job = GlobalScope.launch {
            log(1)
            val r0 = nonCancellableFunction()
            log(2, r0)
            val r1 = cancellableFunction()
            log(3, r1)
        }

        job.invokeOnCancel { log("invoke on cancel") }
        job.cancel()

    }

    suspend fun nonCancellableFunction() = suspendCoroutine<Int> { continuation ->
        val completableFuture = CompletableFuture.supplyAsync {
            Thread.sleep(1000)
            Random.nextInt()
        }

        completableFuture.thenApply {
            continuation.resume(it)
        }.exceptionally {
            continuation.resumeWithException(it)
        }
    }

    suspend fun cancellableFunction() = suspendCancellableCoroutine<Int> { continuation ->
        val completableFuture = CompletableFuture.supplyAsync {
            Thread.sleep(1000)
            Random.nextInt()
        }
        continuation.invokeOnCancellation {
            completableFuture.cancel(true)
        }
        completableFuture.thenApply {
            continuation.resume(it)
        }.exceptionally {
            // when cancelled, `it` will be a CompletionException wrapping a CancellationException.
            continuation.resumeWithException((it as? CompletionException)?.cause ?: it)
        }
    }
}
