package io.sendros.handler

import io.sendros.enums.FileEventsEnum
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Path
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

internal class WatcherHandlerTest {

    private lateinit var watcherHandler: WatcherHandler
    private val tempDirVariable = System.getProperty("java.io.tmpdir")
    private val dummyVoidCallback = DummyVoidCallback()
    private lateinit var tempFilePath: Path
    private val MAX_IO_TIMEOUT = 2L

    fun setUp() {
        thread(start = true) {
            this.tempFilePath = File(tempDirVariable).toPath()
            watcherHandler = WatcherHandler(tempDirVariable, *FileEventsEnum.values())
            watcherHandler.startWatching(dummyVoidCallback)
        }
    }

    @Test
    fun startWatchingEvents() {
        setUp()
        TimeUnit.SECONDS.sleep(MAX_IO_TIMEOUT)
        // CREATE
        assert(!dummyVoidCallback.isOnCreateTriggered)
        tempFilePath.toFile().createNewFile()


        // EDIT
        tempFilePath.toFile().setLastModified(Date().time)
        assert(!dummyVoidCallback.isOnEditTriggered)

        // DELETE
        tempFilePath.toFile().delete()
        assert(!dummyVoidCallback.isOnDeleteTriggered)

        watcherHandler.stopWatching()


    }
}