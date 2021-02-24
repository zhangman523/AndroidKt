package zhangman.github.androidkt.coroutine.base

import kotlinx.coroutines.*

/**
 * Created by admin on 2021/2/6 21:55.
 * Email: zhangman523@126.com
 */

fun main() {
    cancelJob()
    println("----------------")
    cancelCoroutines()
    println("----------------")
    makeCalculateCancel()
    println("----------------")
    finallyCancelJob()
    println("----------------")
    runNonCancellable()
    println("----------------")
//    timeoutJob()
    println("----------------")
    timeoutWithoutException()
    println("----------------")
    asynchronousTimeoutAndResources()

}

/**
 * 取消协程的执行
 * 在一个长时间运行的应用程序中，你也许需要对你的后台协程进行细粒度的控制。
 * 比如说，一个用户也许关闭了一个启动了协程的界面，那么现在协程的执行结果已经不再被需要了，
 * 这时，它应该是可以被取消的。 该 launch 函数返回了一个可以被用来取消运行中的协程的 Job：
 */
fun cancelJob() = runBlocking {
    val job = launch {
        repeat(1000) { i ->
            println("Job: I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L)//延迟一段时间
    println("main: I'm tried of waiting!")
    job.cancel()//取消该作业
    job.join()//等待作业结束
    //or  job.cancelAndJoin()
    println("main: Now I can quit")
}

/**
 * 协程的取消是 协作 的。
 * 一段协程代码必须协作才能被取消。
 * 所有 kotlinx.coroutines 中的挂起函数都是 可被取消的 。
 * 它们检查协程的取消， 并在取消时抛出 CancellationException。
 * 然而，如果协程正在执行计算任务，并且没有检查取消的话，那么它是不能被取消的，
 * 就如如下示例代码所示：
 */
fun cancelCoroutines() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) {//一个执行计算的循环，只为了占用CPU
            //每秒打印消息两次
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("Job: I'm sleeping ${i++}...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()// 取消一个作业并且等待它结束
    println("main: Now I can quit.")
}

/**
 * 我们有两种方法来使执行计算的代码可以被取消。
 * 第一种方法是定期调用挂起函数来检查取消。
 * 对于这种目的 yield 是一个好的选择。
 * 另一种方法是显式的检查取消状态。让我们试试第二种方法。
 * 将前一个示例中的 while (i < 5) 替换为 while (isActive) 并重新运行它。
 */
fun makeCalculateCancel() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (isActive) {//可以被取消的计算循环
            //每秒打印消息两次
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++}...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()// 取消一个作业并且等待它结束
    println("main: Now I can quit.")
}

/**
 * 在 finally 中释放资源
 * 我们通常使用如下的方法处理在被取消时抛出
 * CancellationException 的可被取消的挂起函数。
 * 比如说，try {……} finally {……} 表达式以及 Kotlin 的 use 函数一般在协程被取消的时候执行它们的终结动作：
 */
fun finallyCancelJob() = runBlocking {

    val job = launch {
        try {
            repeat(1000) { i ->
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            println("job: I'm running finally ")
        }
    }
    delay(1300L)//延迟一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit.")
}

/**
 * 运行不能取消的代码块
 * 在前一个例子中任何尝试在 finally 块中调用挂起函数的行为都会抛出
 * CancellationException，因为这里持续运行的代码是可以被取消的。
 * 通常，这并不是一个问题，所有良好的关闭操作（关闭一个文件、取消一个作业、或是关闭任何一种通信通道）
 * 通常都是非阻塞的，并且不会调用任何挂起函数。
 * 然而，在真实的案例中，当你需要挂起一个被取消的协程，你
 * 可以将相应的代码包装在 withContext(NonCancellable) {……} 中，
 * 并使用 withContext 函数以及 NonCancellable 上下文，见如下示例所示：
 */
fun runNonCancellable() = runBlocking {
    val job = launch {
        try {
            repeat(1000) { i ->
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            withContext(NonCancellable) {
                println("job: I'm running finally")
                delay(1000L)
                println("job: And I've just delayed for 1 sec because I'm non-cancellable")
            }
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit.")
}

/**
 * 超时
 * 在实践中绝大多数取消一个协程的理由是它有可能超时。
 * 当你手动追踪一个相关 Job 的引用并启动了一个单独的协程在延迟后取消追踪，
 * 这里已经准备好使用 withTimeout 函数来做这件事。 来看看示例代码：
 */
fun timeoutJob() = runBlocking {
    withTimeout(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
}

/**
 * withTimeout 抛出了 TimeoutCancellationException，它是 CancellationException 的子类。
 * 我们之前没有在控制台上看到堆栈跟踪信息的打印。
 * 这是因为在被取消的协程中 CancellationException 被认为是协程执行结束的正常原因。
 * 然而，在这个示例中我们在 main 函数中正确地使用了 withTimeout。
 * 由于取消只是一个例外，所有的资源都使用常用的方法来关闭。
 * 如果你需要做一些各类使用超时的特别的额外操作，可以使用类似 withTimeout 的 withTimeoutOrNull 函数，
 * 并把这些会超时的代码包装在 try {...} catch (e: TimeoutCancellationException) {...} 代码块中，
 * 而 withTimeoutOrNull 通过返回 null 来进行超时操作，从而替代抛出一个异常：
 */
fun timeoutWithoutException() = runBlocking {
    var result = withTimeoutOrNull(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
        "Done" //在它运行得到结果之前取消它
    }
    println("Result is $result")
}

//---------------------------
var acquired = 0

class Resource {
    init {
        acquired++// Acquire the resource
    }

    fun close() {
        acquired--//Release the resource
    }
}

/**
 * Asynchronous timeout and resources
 * The timeout event in withTimeout is asynchronous with respect to
 * the code running in its block and may happen at any time,
 * even right before the return from inside of the timeout block.
 * Keep this in mind if you open or acquire some resource inside
 * the block that needs closing or release outside of the block.
 * For example, here we imitate a closeable resource with the Resource class,
 * that simply keeps track of how many times it was created by incrementing
 * the acquired counter and decrementing this counter from its close function.
 * Let us run a lot of coroutines with the small timeout try acquire
 * this resource from inside of the withTimeout block after a bit of delay and release it from outside.
 */
fun asynchronousTimeoutAndResources() {
    runBlocking {
        repeat(100_000) {//Launch 100K coroutines
            launch {
                val resource = withTimeout(60) {//Timeout of 60ms
                    delay(50) // Delay for 50ms
                    Resource()// Acquire a resource and return it from withTimeout block
                }
                resource.close()// Release the resource
            }
        }
    }
    // Outside of runBlocking all coroutines have completed
    println(acquired)// Print the number of resources still acquired
}