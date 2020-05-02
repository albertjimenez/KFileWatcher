package io.sendros.handler

import io.sendros.enums.FileEventsEnum
import io.sendros.enums.toFileEventEnum
import io.sendros.interfaces.VoidCallback
import io.sendros.singleton.WatcherServiceInit
import io.sendros.singleton.register
import mu.KotlinLogging
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread


/**
 * Starting class to handle the file events
 * @param pathName the pathname that will be used for checking
 * @param eventsToAttend vararg of all the possible events that can be triggered
 */
class WatcherHandler(private val pathName: String, vararg eventsToAttend: FileEventsEnum) {
    private val watchService: WatchService =
        WatcherServiceInit.initWatcher(pathName, *eventsToAttend)
    private val eventsToAttendInternal = eventsToAttend
    private val logger = KotlinLogging.logger {}
    private val stopWatching: AtomicBoolean = AtomicBoolean(false)


    /**
     * Function to start watching the directory specified on the constructor
     * @param voidCallback The set of operations when the event is triggered
     */
    fun startWatching(voidCallback: VoidCallback) {
        do {
            if (stopWatching.get())
                Thread.currentThread().interrupt()
            val watchKey = watchService.take()
            for (event in watchKey.pollEvents()) {
                val myFile = File(pathName).toPath().resolve(event.context() as Path).toFile()
                val typeEvent: FileEventsEnum = event.kind().name().toFileEventEnum()
                if (typeEvent in eventsToAttendInternal) {
                    logger.info { "Type of event: $typeEvent Context of the file/directory: ${event.context()}" }
                    try {
                        thread(start = true) {
                            if (myFile.isDirectory)
                                registerAll(myFile.toPath())
                            when (typeEvent) {
                                FileEventsEnum.CREATE -> voidCallback.onCreate(myFile)
                                FileEventsEnum.EDIT -> voidCallback.onEdit(myFile)
                                FileEventsEnum.DELETE -> voidCallback.onDelete(myFile)
                            }
                            logger.info { "Thread started successfully for ${myFile.name} on ${myFile.path}" }
                        }
                    } catch (e: FileNotFoundException) {
                        logger.error { "File not found $e on path $pathName" }
                    }
                }
            }
            if (!watchKey.reset())
                break
        } while (watchKey != null && !stopWatching.get())
    }

    private fun registerAll(start: Path) {
        // register directory and sub-directories
        Files.walkFileTree(start, object : SimpleFileVisitor<Path>() {
            override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                dir.register(
                    watchService,
                    eventsToAttendInternal
                )
                return FileVisitResult.CONTINUE
            }
        })
    }

    fun stopWatching() {
        logger.debug { "Stopping watcher on $pathName on ${Date().toInstant()}" }
        thread(start = true) {
            stopWatching.set(true)
        }
    }

}