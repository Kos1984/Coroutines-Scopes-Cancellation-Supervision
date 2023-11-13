
import kotlinx.coroutines.*
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

// Вопросы: Cancellation
// Вопрос 1
/*
fun main() = runBlocking {
    val job = CoroutineScope(EmptyCoroutineContext).launch {
        launch {
            delay(500)
            println("ok") // <-- нет так как job.cancelAndJoin() отменит все корутины раньше
        }
        launch {
            delay(500)
            println("ok")
        }
    }
    delay(100)
    job.cancelAndJoin()
} */
// Вопрос 2
/*
fun main() = runBlocking {
    val job = CoroutineScope(EmptyCoroutineContext).launch {
        val child = launch {
            delay(500)
            println("ok") // <-- нет , child.cancel() отменит корутину раньше
        }
        launch {
            delay(500)
            println("ok")
        }
        delay(100)
        child.cancel()
    }
    delay(100)
    job.join()
}*/

//Вопросы: Exception Handling
// Вопрос 1
/*
fun main() {
    with(CoroutineScope(EmptyCoroutineContext)) {
        try {
            launch {
                throw Exception("something bad happened")
            }
        } catch (e: Exception) {
           e.printStackTrace() // <--блок catch не сработатет так как корутина запущена внутри блока try
        }
    }
    Thread.sleep(1000)
} */

// Вопрос 2
/*
fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        try {
              coroutineScope {
                    throw Exception("something bad happened")

            }
        } catch (e: Exception) {
            e.printStackTrace() // <-- сработает coroutineScope передаст исключение в catch родительской корутины.
        }
    }
    Thread.sleep(1000)
}*/
// Вопрос 3
/*
fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        try {
            supervisorScope {
                throw Exception("something bad happened")
            }
        } catch (e: Exception) {
            e.printStackTrace() // <-- сраотает supervisorScope передаст исключение в блок catch родительской корутины.
        }
    }
    Thread.sleep(1000)
}*/
//Вопрос 4
/*
fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        try {
            coroutineScope {
                launch {
                    delay(500)
                    throw Exception("something bad happened") // <-- нет, вторая дочерняя корутитна отменит текущую и родительскую
                }
                launch {
                    throw Exception("something bad happened")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    Thread.sleep(1000)
} */
// Вопрос 5
/*
fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        try {
            supervisorScope {
                launch {
                    //delay(100)
                    throw Exception("something bad happened1") // <-- да так как supervisorScope позволит сделать это несмотря на ошибку в другой дочерней корутине
                }
                launch {
                    throw Exception("something bad happened2")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace() // <-- нет так как supervisorScope обрабоботает исключение внутри себя
        }
    }
    Thread.sleep(1000)
} */
//Вопрос 6
/*
fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        CoroutineScope(EmptyCoroutineContext).launch {
            launch {
                delay(1000)
                println("ok") // <-- нет так как исключение в родительсой корутине сработате раньше и отменит все дочерние.
            }
            launch {
                delay(500)
                println("ok")
            }
            throw Exception("something bad happened")
        }
    }
    Thread.sleep(1000)
}*/
//Вопрос 7
/*
fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        CoroutineScope(EmptyCoroutineContext + SupervisorJob()).launch {
            launch {
                delay(1000)
                println("ok") // <-- нет так как ошибка в родительской корутине отменит все дочерние
            }
            launch {
                delay(500)
                println("ok")
            }
            throw Exception("something bad happened")
        }
    }
    Thread.sleep(1000)
}
*/