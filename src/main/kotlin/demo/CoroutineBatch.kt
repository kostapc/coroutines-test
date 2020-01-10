package demo

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CoroutineBatch {

    fun doWork(tasks : Int) {
        val items : MutableList<Int> = ArrayList()
        (1..tasks).forEach {
            items.add(it)
        }

        val jobs : MutableList<Job> = ArrayList()
        items.forEach {
            jobs.add(GlobalScope.launch {
                delay(1000)
                println("$it task done")
            })
        }

        runBlocking {
            jobs.forEach { it.join() }
        }
    }

}