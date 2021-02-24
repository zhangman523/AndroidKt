package zhangman.github.androidkt.coroutine.base

import kotlinx.coroutines.*

/**
 * Created by admin on 2021/2/6 17:09.
 * Email: zhangman523@126.com
 */
fun main() {
    GlobalScope.launch { // 在后台启动一个新的协程并继续
        delay(1000L)
        println("World!")
    }
    println("Hello,") // 主线程中的代码会立即执行
    runBlocking {     // 但是这个表达式阻塞了主线程
        delay(2000L)  // ……我们延迟 2 秒来保证 JVM 的存活
    }

    runBlocking {
        awaitCoroutine()
    }

    coroutineScopeTest()

//    testRepeatCoroutines()

    runBlocking {
        testGlobalScopeLikeDaemon()
        delay(1300L)
    }
}

/**
 * 它启动了 10 万个协程，并且在 5 秒钟后，每个协程都输出一个点
 */
suspend fun awaitCoroutine() {
    val job = GlobalScope.launch {
        delay(1000L)
        println("World!")
    }
    println("Hello,")
    job.join()//等待直到子协程执行结束
}


fun coroutineScopeTest() = runBlocking {
    launch {
        delay(200L)
        println("Task from runBlocking")
    }
    coroutineScope { //创建一个协程作用域
        launch {
            delay(500L)
            println("Task from nested launch")
        }
        delay(100L)
        println("Task from coroutine scope")//这一行会在内嵌launch 之前输出
    }
    println("Coroutine scope is over")//这一行在内嵌launch 执行完毕后输出
}

fun testRepeatCoroutines() = runBlocking {
    repeat(100_000){//启动大量的协程
        launch {
            delay(5000L)
            print(".")
        }
    }
}

/**
 * 以下代码在 GlobalScope 中启动了一个长期运行的协程，该协程每秒输出“I'm sleeping”两次，之后在主函数中延迟一段时间后返回。
 */
fun testGlobalScopeLikeDaemon() = GlobalScope.launch {
    repeat(1000) { i ->
        println("I'm sleeping $i ...")
        delay(500L)
    }
}

