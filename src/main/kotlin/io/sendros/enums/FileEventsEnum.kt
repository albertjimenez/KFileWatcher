package io.sendros.enums

import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchEvent

enum class FileEventsEnum {
    CREATE, EDIT, DELETE;

    fun toStandardEvents(): WatchEvent.Kind<Path>? = when (this) {
        CREATE -> StandardWatchEventKinds.ENTRY_CREATE
        EDIT -> StandardWatchEventKinds.ENTRY_MODIFY
        DELETE -> StandardWatchEventKinds.ENTRY_DELETE
    }
}

fun String.toFileEventEnum(): FileEventsEnum = when (this.toUpperCase()) {
    "ENTRY_CREATE" -> FileEventsEnum.CREATE
    "ENTRY_MODIFY" -> FileEventsEnum.EDIT
    "ENTRY_DELETE" -> FileEventsEnum.DELETE
    else -> FileEventsEnum.EDIT

}
