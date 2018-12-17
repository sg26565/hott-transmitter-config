import kotlin.concurrent.thread

fun main(args: Array<String>) {
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