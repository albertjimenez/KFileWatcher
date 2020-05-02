package io.sendros.singleton

import io.sendros.enums.FileEventsEnum
import io.sendros.handler.WatcherHandler
import java.nio.file.*

/**
 * Singleton for a thread-safe environment that constructs the [WatchService] instance needed on [WatcherHandler]
 */
object WatcherServiceInit {
    private val watchService: WatchService = FileSystems.getDefault().newWatchService()

    /**
     * Static method that constructs the [WatchService] called only by [WatcherHandler] otherwise it will throw
     * an [IllegalAccessException]
     */
    fun initWatcher(pathName: String, vararg fileEventsEnum: FileEventsEnum): WatchService {
        if (WatcherHandler::class.java.name !in Thread.currentThread().stackTrace.map { it.className })
            throw IllegalAccessException("Method ${this::initWatcher.name} called outside ${WatcherHandler::class.java.name}")
        val path: Path = Paths.get(pathName)
        path.register(watchService, fileEventsEnum)
        return watchService
    }
}

/**
 * Extension function to allow interoperability between [FileEventsEnum] and [WatchEvent.Kind]
 */
fun Path.register(watchService: WatchService?, fileEventsEnum: Array<out FileEventsEnum>) {
    register(watchService, fileEventsEnum.map { it.toStandardEvents() }.toTypedArray())
}
