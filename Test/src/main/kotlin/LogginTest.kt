import java.util.logging.Level
import java.util.logging.Logger

fun main(vararg args: String) {
    val url = ClassLoader.getSystemResource("logging.properties")
    System.setProperty("java.util.logging.config.file", url.path)

    val className = "LoggingTest"
    val methodName = "main"
    val exception = Exception()
    val log = Logger.getLogger(className)

    log.entering(className, methodName, args)
    log.finest("finest")
    log.finer("finer")
    log.fine("fine")
    log.throwing(className, methodName, exception)
    log.config("config")
    log.info("info")
    log.warning("warning")
    log.severe("severe")
    log.exiting(className,methodName,null)

    log.log(Level.INFO, "message with parameter {0}", "foo")

}