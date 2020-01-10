package demo

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.system.exitProcess

val pool: Executor = Executors.newFixedThreadPool(4)

suspend fun realAsyncJavaCall() {
    val channel = Channel<Unit>()
    pool.execute {
        LongExecution().exec()
        channel.offer(Unit)
    }
    withContext(Dispatchers.IO) {
        channel.receive()
    }
}

suspend fun asyncJavaCall() {
    withContext(Dispatchers.IO) {
        LongExecution().exec()
    }
}

fun getValueWithLongDelay(value: Any): String {
    //LongExecution().exec() // direct call

    runBlocking {
        //delay(1000) // suspend, but not blocking coroutine, different behave with java Thread.sleep()
        //asyncJavaCall() // wrapped call
        realAsyncJavaCall() // call with passing execution to thread pool
    }
    return "val $value"
}

fun syncVars() {
    var time = System.currentTimeMillis()
    val one = getValueWithLongDelay("one")
    val two = getValueWithLongDelay("two")
    val three = getValueWithLongDelay("three")

    val asyncTime = System.currentTimeMillis() - time
    println("after sync calls = $asyncTime")
    var awaitTime = System.currentTimeMillis()
    println("value: $one")
    println("value: $two")
    println("value: $three")
    awaitTime = System.currentTimeMillis() - awaitTime
    println("after print time: $awaitTime")

    time = System.currentTimeMillis() - time
    println("exec time: $time")
}

fun asyncVars() {
    var time = System.currentTimeMillis()
    runBlocking {
        coroutineScope {
            val one = async{getValueWithLongDelay("one")}
            val two = async{getValueWithLongDelay("two")}
            val three = async{getValueWithLongDelay("three")}

            val asyncTime = System.currentTimeMillis() - time
            println("after async calls = $asyncTime")
            var awaitTime = System.currentTimeMillis()
            println("value: ${one.await()}")
            println("value: ${two.await()}")
            println("value: ${three.await()}")
            awaitTime = System.currentTimeMillis() - awaitTime
            println("after await time: $awaitTime")
        }
    }
    time = System.currentTimeMillis() - time
    println("exec time: $time")
}

fun asyncAndAwaitArr() {

    var time = System.currentTimeMillis()

    val results: MutableList<Deferred<String>> = ArrayList()

    runBlocking {
        for (i in 1..3) {
            val value = async { getValueWithLongDelay(i) }
            results.add(value)
        }
    }

    runBlocking {
        results.forEach {
            println("result: ${it.await()}")
        }
    }

    time = System.currentTimeMillis() - time
    println("exec time: $time")
    // exec time: 3085
}

fun launchAndJoinArr() {

    var time = System.currentTimeMillis()

    val results: MutableList<String> = CopyOnWriteArrayList()
    val jobs: MutableList<Job> = ArrayList()

    runBlocking {
        for (i in 1..3) {
            jobs.add(GlobalScope.launch {
                val value = getValueWithLongDelay(i)
                results.add(value)
            })
        }
        jobs.joinAll()
    }

    results.forEach {
        println("result: $it")
    }

    time = System.currentTimeMillis() - time
    println("exec time: $time")
}

fun main() {
    println("using direct call")

    println("async vars")
    asyncVars()
    print("\n\n")

    println("sync vars")
    syncVars()
    print("\n\n")

    println("asyncAndAwaitArr")
    asyncAndAwaitArr()
    print("\n\n")

    println("launchAndJoinArr")
    launchAndJoinArr()
    print("\n\n")


    exitProcess(0)
}