package zhangman.github.androidkt.coroutine.base

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**
 * Created by admin on 2021/2/7 11:39.
 * Email: zhangman523@126.com
 */

fun main() {
    combinationSuspendSequence()
    println("---------next Sample---------")
    combinationSuspendAsync()
    println("---------next Sample---------")
    combinationSuspendLazyAsync()
    println("---------next Sample---------")
    combinationAsync()
    println("---------next Sample---------")
    val time = measureTimeMillis {
        println("The answer is ${runBlocking { concurrentSum() }}")
    }
    println("Completed in $time ms")
    println("---------next Sample---------")
}

/**
 * 我们使用普通的顺序来进行调用，因为这些代码是运行在协程中的，只要像常规的代码一样 顺序 都是默认的。
 * 下面的示例展示了测量执行两个挂起函数所需要的总时间：
 */
fun combinationSuspendSequence() = runBlocking {
    val time = measureTimeMillis {
        val one = doSomethingUseFulOne()
        val two = doSomethingUsefulTwo()
        println(" The answer is ${one + two}");
    }
    println("Completed in $time ms")
}

/**
 * 在概念上，async 就类似于 launch。它启动了一个单独的协程，这是一个轻量级的线程并与其它所有的协程一起并发的工作。
 * 不同之处在于 launch 返回一个 Job 并且不附带任何结果值，而 async 返回一个 Deferred —— 一个轻量级的非阻塞 future，
 * 这代表了一个将会在稍后提供结果的 promise。
 * 你可以使用 .await() 在一个延期的值上得到它的最终结果， 但是 Deferred 也是一个 Job，所以如果需要的话，你可以取消它。
 */
fun combinationSuspendAsync() = runBlocking {
    val time = measureTimeMillis {
        val one = async { doSomethingUseFulOne() }
        val two = async { doSomethingUsefulTwo() }
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")
}

/**
 * 惰性启动的 async
 * 可选的，async 可以通过将 start 参数设置为 CoroutineStart.LAZY 而变为惰性的。
 * 在这个模式下，只有结果通过 await 获取的时候协程才会启动，或者在 Job 的 start 函数调用的时候。运行下面的示例：
 */
fun combinationSuspendLazyAsync() = runBlocking {
    val time = measureTimeMillis {
        val one = async(start = CoroutineStart.LAZY) { doSomethingUseFulOne() }
        val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
        //执行一些计算
        one.start()//启动第一个
        two.start()//启动第二个
        println(" The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")
}


/**
 * 这些 xxxAsync 函数不是 挂起 函数。它们可以在任何地方使用。
 * 然而，它们总是在调用它们的代码中意味着异步（这里的意思是 并发 ）执行。
 */
fun combinationAsync() {
    val time = measureTimeMillis {
        // 我们可以在协程外面启动异步执行
        val one = somethingUsefulOneAsync()
        val two = somethingUsefulTwoAsync()
        // 但是等待结果必须调用其它的挂起或者阻塞
        // 当我们等待结果的时候，这里我们使用 `runBlocking { …… }` 来阻塞主线程
        runBlocking {
            println("The answer is ${one.await() + two.await()}")
        }
    }
    println("Complete in $time ms")
}

/**
 * 让我们使用使用 async 的并发这一小节的例子并且提取出一个函数并发的调用
 * doSomethingUsefulOne 与 doSomethingUsefulTwo 并且返回它们两个的结果之和。
 * 由于 async 被定义为了 CoroutineScope 上的扩展，
 * 我们需要将它写在作用域内，并且这是 coroutineScope 函数所提供的：
 */
suspend fun concurrentSum(): Int = coroutineScope {
    val one = async { doSomethingUseFulOne() }
    val two = async { doSomethingUsefulTwo() }
    one.await() + two.await()
}

suspend fun doSomethingUseFulOne(): Int {
    delay(1000L)
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L)
    return 29
}

fun somethingUsefulOneAsync() = GlobalScope.async {
    doSomethingUseFulOne()
}

fun somethingUsefulTwoAsync() = GlobalScope.async {
    doSomethingUsefulTwo()
}
