package io.sendros.handler

import io.sendros.interfaces.VoidCallback
import java.io.File

class DummyVoidCallback : VoidCallback {
    var isOnCreateTriggered = false
    var isOnEditTriggered = false
    var isOnDeleteTriggered = false

    override fun onCreate(file: File) {
        println("CREATED FILE: ${file.name} on ${file.path}")
        isOnCreateTriggered = true
    }

    override fun onEdit(file: File) {
        println("EDITED FILE: ${file.name} on ${file.path}")
        isOnEditTriggered = true
    }

    override fun onDelete(file: File) {
        println("DELETED FILE: ${file.name} on ${file.path}")
        isOnDeleteTriggered = true
    }
}