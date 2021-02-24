package zhangman.github.androidkt.coroutine.base

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis

/**
 * Created by admin on 2021/2/7 17:04.
 * Email: zhangman523@126.com
 */
fun main() {
    simple().forEach { value -> println(value) }
    println("-------------------------")
    simpleSequence().forEach { value -> println(value) }
    println("-------------------------")
    runBlocking {
        simpleSuspend().forEach { value -> println(value) }
    }
    println("-------------------------")
    runBlocking {
        simpleFlow().collect { value -> println(value) }
    }
    println("-------------------------")
    runBlocking {
        println("Calling simpleFlow function...")
        val flow = simpleFlow()
        println("Calling collect... ")
        flow.collect { value -> println(value) }
        println("Calling collect again ...")
        flow.collect { value -> println(value) }
    }
    println("-------------------------")
    runBlocking {
        withTimeoutOrNull(250) {//在 250毫秒后超时
            simpleFlow().collect { value -> println(value) }
        }
        println("Done")
    }
    println("-------------------------")
    runBlocking {
        // 将一个整数区间转化为流
        (1..3).asFlow().collect { value -> println(value) }
    }
    println("-------------------------")
    simpleMapFlow()
    println("-------------------------")
    simpleTransform()
    println("-------------------------")
    simpleTakeFlow()
    println("-------------------------")
    simpleReduceFlow()
    println("-------------------------")
    simpleContinueFlow()
    println("-------------------------")
//    simpleWithContext()
    simpleFlowOn()
    println("-------------------------")
    simpleBuffer()
    println("-------------------------")
    simpleConflate()
    println("-------------------------")
    simpleCollectLatest()
    println("-------------------------")
    simpleZip()
    println("-------------------------")
    simpleCombine()
    println("-------------------------")
    simpleFlatMapConcat()
    println("-----------simpleFlatMapMerge--------------")
    simpleFlatMapMerge()
    println("-----------simpleFlatMapLatest--------------")
    simpleFlatMapLatest()
    println("-----------simpleTryCatch--------------")
    simpleTryCatch()
    println("-----------simpleCatch--------------")
    simpleCatch()
    println("-----------simpleCompleteFinally--------------")
    simpleCompleteFinally()
    println("-----------simpleCompletion--------------")
    simpleCompletion()
    println("-----------simpleStartFlow--------------")
    simpleStartFlow()
    println("-----------simpleLaunchIn--------------")
    simpleLaunchIn()
    println("-----------simpleFlowCancel--------------")
//    simpleFlowCancel()// exception

}

fun simple(): List<Int> = listOf(1, 2, 3)


fun simpleSequence(): Sequence<Int> = sequence {
    for (i in 1..3) {
        Thread.sleep(100)//假装我们在计算
        yield(i)
    }
}

suspend fun simpleSuspend(): List<Int> {
    delay(1000)
    return listOf(1, 2, 3)
}

/**
 * 流是冷的
 * Flow 是一种类似于序列的冷流 — 这段 flow 构建器中的代码直到流被收集的时候才运行。
 * 这在以下的示例中非常明显：
 */
fun simpleFlow(): Flow<Int> = flow {//流构建器
    println("Flow started")
    for (i in 1..3) {
        delay(100)
        println("Emitting $i")
        emit(i)//发送下一个值
    }
}

suspend fun performReqeust(request: Int): String {
    delay(1000)//模仿长时间运行的异步工作
    return "response $request"
}

/**
 * 过渡流操作符
 * 可以使用操作符转换流，就像使用集合与序列一样。
 * 过渡操作符应用于上游流，并返回下游流。 这些操作符也是冷操作符，就像流一样。
 * 这类操作符本身不是挂起函数。它运行的速度很快，返回新的转换流的定义。
 * 基础的操作符拥有相似的名字，比如 map 与 filter。
 * 流与序列的主要区别在于这些操作符中的代码可以调用挂起函数。
 * 举例来说，一个请求中的流可以使用 map 操作符映射出结果，即使执行一个长时间的请求操作也可以使用挂起函数来实现：
 */
fun simpleMapFlow() = runBlocking {
    (1..3).asFlow().map { request -> performReqeust(request) }
        .collect { response -> println(response) }
}

/**
 *转换操作符
 * 在流转换操作符中，最通用的一种称为 transform。
 * 它可以用来模仿简单的转换，例如 map 与 filter，以及实施更复杂的转换。
 * 使用 transform 操作符，我们可以 发射 任意值任意次。
 * 比如说，使用 transform 我们可以在执行长时间运行的异步请求之前发射一个字符串并跟踪这个响应：
 */
fun simpleTransform() = runBlocking {
    (1..3).asFlow()
        .transform { request ->
            emit("Making request $request")
            emit(performReqeust(request))
        }.collect { response -> println(response) }
}

/**
 * 限长操作符
 * 限长过渡操作符（例如 take）在流触及相应限制的时候会将它的执行取消。
 * 协程中的取消操作总是通过抛出异常来执行，这样所有的资源管理函数（如 try {...} finally {...} 块）会在取消的情况下正常运行：
 */
fun simpleTakeFlow() = runBlocking {
    flow {
        try {
            emit(1)
            emit(2)
            println("This line will not execute")
            emit(3)
        } finally {
            println("Finally in numbers")
        }
    }.take(2)// 只获取前两个
        .collect { value -> println(value) }
}

/**
 * 末端流操作符
 * 末端操作符是在流上用于启动流收集的挂起函数。 collect 是最基础的末端操作符，但是还有另外一些更方便使用的末端操作符：
 * 转化为各种集合，例如 toList 与 toSet。
 * 获取第一个（first）值与确保流发射单个（single）值的操作符。
 * 使用 reduce 与 fold 将流规约到单个值。
 * 举例来说：
 */
fun simpleReduceFlow() = runBlocking {
    val sum = (1..5).asFlow()
        .map { it * it }//数字1至5的平方
        .reduce { a, b -> a + b }
    println(sum)
}

/**
 * 流是连续的
 * 流的每次单独收集都是按顺序执行的，除非进行特殊操作的操作符使用多个流。
 * 该收集过程直接在协程中运行，该协程调用末端操作符。 默认情况下不启动新协程。
 * 从上游到下游每个过渡操作符都会处理每个发射出的值然后再交给末端操作符。
 * 请参见以下示例，该示例过滤偶数并将其映射到字符串：
 */
fun simpleContinueFlow() = runBlocking {
    (1..5).asFlow().filter {
        println("Filter $it")
        it % 2 == 0
    }.map {
        println("Map $it")
        "string $it"
    }.collect {
        println("Collect $it")
    }
}

/**
 * 流上下文
 * 流的收集总是在调用协程的上下文中发生。
 * withContext 发出错误
 * 长时间运行的消耗 CPU 的代码也许需要在 Dispatchers.Default 上下文中执行，
 * 并且更新 UI 的代码也许需要在 Dispatchers.Main 中执行。
 * 通常，withContext 用于在 Kotlin 协程中改变代码的上下文，但是 flow {...}
 * 构建器中的代码必须遵循上下文保存属性，并且不允许从其他上下文中发射（emit）。
 */
fun simpleWithContext() = runBlocking {
    flow {
        kotlinx.coroutines.withContext(Dispatchers.Default) {
            for (i in 1..3) {
                Thread.sleep(100)
                emit(i)
            }
        }
    }.collect { value -> println(value) }
}

/**
 * flowOn 操作符
 * 例外的是 flowOn 函数，该函数用于更改流发射的上下文。
 * 以下示例展示了更改流上下文的正确方法，该示例还通过打印相应线程的名字以展示它们的工作方式：
 */
fun simpleFlowOn() = runBlocking {
    flow {
        for (i in 1..3) {
            Thread.sleep(100)
            println("Emitting $i")
            emit(i)
        }
    }.flowOn(Dispatchers.Default)// 在流构建器中改变消耗 CPU 代码上下文的正确方式
        .collect { value ->
            println("Collected $value")
        }
}

/**
 * 缓冲
 * 从收集流所花费的时间来看，将流的不同部分运行在不同的协程中将会很有帮助，
 * 特别是当涉及到长时间运行的异步操作时
 * 我们可以在流上使用 buffer 操作符来并发运行这个 simple 流中发射元素的代码以及收集的代码， 而不是顺序运行它们：
 */
fun simpleBuffer() = runBlocking {
    println("----没有用buffer操作符---")
    val time = measureTimeMillis {
        flow {
            for (i in 1..3) {
                delay(100)
                emit(i)
            }
        }.collect { value ->
            delay(300)
            println(value)
        }
    }
    println("Collected in $time ms")
    println("----用buffer操作符---")
    val timeBuffer = measureTimeMillis {
        flow {
            for (i in 1..3) {
                delay(100)
                emit(i)
            }
        }.buffer()//缓冲发射项，无需等待
            .collect { value ->
                delay(300)
                println(value)
            }
    }
    println("Collected in $timeBuffer ms")
}

/**
 * 合并
 * 当流代表部分操作结果或操作状态更新时，
 * 可能没有必要处理每个值，而是只处理最新的那个。
 * 在本示例中，当收集器处理它们太慢的时候， conflate 操作符可以用于跳过中间值。构建前面的示例
 */
fun simpleConflate() = runBlocking {
    val time = measureTimeMillis {
        flow {
            for (i in 1..3) {
                delay(100)
                emit(i)
            }
        }.conflate()
            .collect { value ->
                delay(300)
                println(value)
            }
    }
    println("Collected in $time ms")
}

/**
 * 处理最新值
 * 当发射器和收集器都很慢的时候，合并是加快处理速度的一种方式。
 * 它通过删除发射值来实现。 另一种方式是取消缓慢的收集器，并在每次发射新值的时候重新启动它。
 * 有一组与 xxx 操作符执行相同基本逻辑的 xxxLatest 操作符，但是在新值产生的时候取消执行其块中的代码。
 * 让我们在先前的示例中尝试更换 conflate 为 collectLatest：
 */
fun simpleCollectLatest() = runBlocking {
    val time = measureTimeMillis {
        flow {
            for (i in 1..3) {
                delay(100)
                emit(i)
            }
        }.collectLatest { value ->
            println("Collecting $value")
            delay(300)
            println("Done $value")
        }
    }
    println("Collected in $time ms")
}

/**
 * Zip
 * 就像 Kotlin 标准库中的 Sequence.zip 扩展函数一样， 流拥有一个 zip 操作符用于组合两个流中的相关值：
 */
fun simpleZip() = runBlocking {
    val nums = (1..3).asFlow()
    val strs = flowOf("one", "two", "three")
    nums.zip(strs) { a, b -> "$a -> $b" }
        .collect { println(it) }//收集并打印
}

/**
 * Combine
 * 当流表示一个变量或操作的最新值时（请参阅相关小节 conflation），
 * 可能需要执行计算，这依赖于相应流的最新值，并且每当上游流产生值的时候都需要重新计算。
 * 这种相应的操作符家族称为 combine。
 */
fun simpleCombine() = runBlocking {
    println("--- use zip ---")
    val nums = (1..3).asFlow().onEach { delay(300) }//发射数字 1..3，间隔300毫秒
    val strs = flowOf("one", "two", "three").onEach { delay(400) }//每 400 毫秒发射一次字符串
    val startTime = System.currentTimeMillis()//记录开始时间
    nums.zip(strs) { a, b -> "$a -> $b" }
        .collect { value ->
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }
    println("--- use combine ---")
    val combineStartTime = System.currentTimeMillis()
    nums.combine(strs) { a, b -> "$a -> $b" }
        .collect { value ->
            println("$value at ${System.currentTimeMillis() - combineStartTime} ms from start")
        }
}

/**
 *  展平流
 *  连接模式由 flatMapConcat 与 flattenConcat 操作符实现。
 *  它们是相应序列操作符最相近的类似物。
 *  它们在等待内部流完成之前开始收集下一个值，如下面的示例所示：
 */
fun simpleFlatMapConcat() = runBlocking {
    val startTime = System.currentTimeMillis()
    (1..3).asFlow().onEach { delay(100) }//每 100 毫秒发射一个数字
        .flatMapConcat { requestFlow(it) }
        .collect { value ->
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }
}

fun requestFlow(i: Int): Flow<String> = flow {
    emit("$i: First")
    delay(500)
    emit("$i: Second")
}

/**
 * flatMapMerge
 * 另一种展平模式是并发收集所有传入的流，并将它们的值合并到一个单独的流，以便尽快的发射值。
 * 它由 flatMapMerge 与 flattenMerge 操作符实现。
 * 他们都接收可选的用于限制并发收集的流的个数的 concurrency 参数（默认情况下，它等于 DEFAULT_CONCURRENCY）。
 */
fun simpleFlatMapMerge() = runBlocking {
    val startTime = System.currentTimeMillis()
    (1..3).asFlow().onEach { delay(100) }
        .flatMapMerge { requestFlow(it) }
        .collect { value ->
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }
}

/**
 * flatMapLatest
 * 与 collectLatest 操作符类似（在"处理最新值" 小节中已经讨论过），
 * 也有相对应的“最新”展平模式，在发出新流后立即取消先前流的收集。
 * 这由 flatMapLatest 操作符来实现。
 */
fun simpleFlatMapLatest() = runBlocking {
    val startTime = System.currentTimeMillis()
    (1..3).asFlow().onEach { delay(100) }// 每 100 毫秒发射一个数字
        .flatMapLatest { requestFlow(it) }
        .collect { value ->
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }
}

/**
 * 流异常
 * 当运算符中的发射器或代码抛出异常时，流收集可以带有异常的完成。 有几种处理异常的方法。
 * 收集器 try 与 catch
 * 收集者可以使用 Kotlin 的 try/catch 块来处理异常：
 */
fun simpleTryCatch() = runBlocking {
    try {
        flow {
            for (i in 1..3) {
                println("Emitting $i")
                emit(i)
            }
        }.collect { value ->
            check(value <= 1) {
                "Collected $value"
            }
        }
    } catch (e: Throwable) {
        println("Caught $e")
    }

}

/**
 * 声明式捕获
 * 我们可以将 catch 操作符的声明性与处理所有异常的期望相结合，
 * 将 collect 操作符的代码块移动到 onEach 中，并将其放到 catch 操作符之前。
 * 收集该流必须由调用无参的 collect() 来触发
 */
fun simpleCatch() = runBlocking {
    flow {
        for (i in 1..3) {
            println("Emitting $i")
            emit(i)
        }
    }.onEach { value ->
        check(value <= 1) { "Collected $value" }
        println(value)
    }.catch { e -> println("Caught $e") }
        .collect()
}

/**
 * 流完成
 * 当流收集完成时（普通情况或异常情况），它可能需要执行一个动作。 你可能已经注意到，它可以通过两种方式完成：命令式或声明式。
 * 命令式 finally 块
 * 除了 try/catch 之外，收集器还能使用 finally 块在 collect 完成时执行一个动作
 */
fun simpleCompleteFinally() = runBlocking {
    try {
        (1..3).asFlow().collect { value -> println(value) }
    } finally {
        println("Done")
    }
}

/**
 * 声明式处理
 * 对于声明式，流拥有 onCompletion 过渡操作符，它在流完全收集时调用。
 * 可以使用 onCompletion 操作符重写前面的示例，并产生相同的输出：
 */
fun simpleCompletion() = runBlocking {
    (1..3).asFlow()
        .onCompletion { println("Done") }
        .collect { value -> println(value) }
}

/**
 *启动流
 * 使用流表示来自一些源的异步事件是很简单的。 在这个案例中，我们需要一个类似 addEventListener 的函数，
 * 该函数注册一段响应的代码处理即将到来的事件，并继续进行进一步的处理。onEach 操作符可以担任该角色。
 * 然而，onEach 是一个过渡操作符。我们也需要一个末端操作符来收集流。 否则仅调用 onEach 是无效的。
 *
 * 如果我们在 onEach 之后使用 collect 末端操作符，那么后面的代码会一直等待直至流被收集
 *
 * 输出:
 * Event: 1
 * Event: 2
 * Event: 3
 * Done
 */
fun simpleStartFlow() = runBlocking {
    val flow = (1..3).asFlow().onEach { delay(100) }

    flow.onEach { event -> println("Event: $event") }
        .collect()//<----等待流收集
    println("Done")
}

/**
 * launchIn 末端操作符可以在这里派上用场。
 * 使用 launchIn 替换 collect 我们可以在单独的协程中启动流的收集，
 * 这样就可以立即继续进一步执行代码：
 *
 * 输出:
 * Done
 * Event: 1
 * Event: 2
 * Event: 3
 */
fun simpleLaunchIn() = runBlocking {
    val flow = (1..3).asFlow().onEach { delay(100) }
    flow.onEach { event -> println("Event: $event") }
        .launchIn(this) // <--- 在单独的协程中执行流
    println("Done")
}

/**
 * 流取消检测
 * 为方便起见，流构建器对每个发射值执行附加的 ensureActive 检测以进行取消。
 * 这意味着从 flow { ... } 发出的繁忙循环是可以取消的
 */
fun simpleFlowCancel() = runBlocking {
    try {
        val flow = flow {
            for (i in 1..5) {
                println("Emitting $i")
                emit(i)
            }
        }

        flow.collect { value ->
            if (value == 3) cancel()
            println(value)
        }
    } catch (e: Throwable) {
        println("Caught: $e")
    }
}

fun simpleFlowCancellable() = runBlocking {
    (1..5).asFlow().collect { value ->
        if (value == 3) cancel()
        println(value)
    }
}