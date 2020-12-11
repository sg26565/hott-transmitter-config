import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.concurrent.thread

fun main1() {
    val lock = Object()

    println("start")

    thread {
        println("thread start")

        Thread.sleep(1000)
        synchronized(lock) {
            println("notify")
            lock.notify()
        }

        println("thread stop")
    }

    synchronized(lock) {
        println("wait")
        lock.wait()
    }

    println("done")
}

class FixedPool(val workerCount: Int) {
    val channel = Channel<Task>()
    val jobs = mutableListOf<Job>()

    init {
        start() // start immediately
    }

    fun start() {
        repeat(workerCount) { i ->
            jobs.add(GlobalScope.launch { // or use your own coroutine context
                for (task in channel) {
                    println("worker-$i starts ${task.name}")
                    task()
                    println("worker-$i finished ${task.name}")
                }
            })
        }
    }

    fun execute(block: Task) {
        GlobalScope.launch(Dispatchers.Unconfined) { // seems safe to use Unconfined
            channel.send(block)
        }
    }

    suspend fun join() {
        for (j in jobs) j.join()
    }

    fun close() = channel.close()
}

class Task(val name: String, val time: Long) {
    operator fun invoke() {
        val start = System.currentTimeMillis()
        val stop = start + time
        while (System.currentTimeMillis() < stop);
    }
}

fun random() = (Math.random() * 10000).toLong()

fun main() {
    runBlocking {
        val pool = FixedPool(8)
        repeat(100) { i->
            pool.execute(Task(i.toString(), random()))
        }

        // We must wait; in long running app maybe not needed
        pool.close()
        pool.join()
    }
}
