import de.treichels.hott.util.Util
import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.Logger

fun main (args: Array<String>) {
    Util.enableLogging()
    val logger = Logger.getGlobal()

    for (l in arrayOf(Level.FINEST, Level.FINER, Level.FINE, Level.CONFIG, Level.INFO, Level.WARNING, Level.SEVERE)) {
        logger.log(l, l.name)
    }

    Foo().bar()
}

class Foo {
    private val log = Logger.getGlobal()

    fun bar() {
        log.entering(this.javaClass.name, "bar")

        println("bar")

        log.exiting(this.javaClass.name, "bar")
    }
}
