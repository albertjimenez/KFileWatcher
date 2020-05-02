package examples

import io.sendros.enums.FileEventsEnum
import io.sendros.handler.WatcherHandler
import io.sendros.interfaces.VoidCallback
import java.io.File

fun main() {
    val watcherHandler =
        WatcherHandler(System.getProperty("java.io.tmpdir"), *FileEventsEnum.values())

    watcherHandler.startWatching(object : VoidCallback {

        val getFileFolderString: ((file: File) -> String) = { file: File ->
            if (file.isFile) "File" else "Folder"
        }

        override fun onCreate(file: File) {
            println("${getFileFolderString(file)} created ${file.name}")
        }

        override fun onEdit(file: File) {
            println("${getFileFolderString(file)} edited ${file.name}")
        }

        override fun onDelete(file: File) {
            println("${getFileFolderString(file)} deleted ${file.name}")
        }
    })
    File(System.getProperty("java.io.tmpdir")).createNewFile().takeIf { it }?.apply {
        println("Created new temporary file")
    }
}