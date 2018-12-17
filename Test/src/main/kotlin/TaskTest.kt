import javafx.application.Application
import javafx.concurrent.Service
import javafx.concurrent.Task
import javafx.concurrent.WorkerStateEvent
import javafx.stage.Stage
import tornadofx.*

fun main(vararg args: String) {
    Application.launch(MyApp1::class.java, *args)
}

class MyApp1 : Application() {
    private val service = MyService()

    override fun start(primaryStage: Stage?) {

        runAsync {
            runLater {
                service.messageProperty().addListener { _, _, newValue -> println("Massage: $newValue") }
                service.addEventHandler(WorkerStateEvent.ANY) {
                    println(it.eventType)

                    when (it.eventType) {
                        WorkerStateEvent.WORKER_STATE_SUCCEEDED -> {
                            println("Result ${service.value}")
                        }
                    }
                }
                service.start()
            }

            Thread.sleep(1000)

            runLater {
                service.restart()
            }
        }
    }
}

class MyTask : Task<Int>() {
    override fun call(): Int {
        println("MyTask start")
        var iterations: Int = 0

        while (iterations < 20) {
            if (isCancelled) {
                println("Cancelled")
                break
            }
            updateMessage("Iteration $iterations")
            updateProgress(iterations.toLong(), 1000)

            //Block the thread for a short time, but be sure
            //to check the InterruptedException for cancellation
            try {
                Thread.sleep(100)
            } catch (interrupted: InterruptedException) {
                if (isCancelled) {
                    println("Interrupted")
                    break
                }
            }

            iterations++
        }

        println("MyTask done")
        return iterations
    }
}

class MyService : Service<Int>() {
    override fun createTask(): Task<Int> = MyTask()
}