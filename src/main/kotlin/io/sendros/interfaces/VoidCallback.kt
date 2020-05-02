package io.sendros.interfaces

import java.io.File

interface VoidCallback {
    fun onCreate(file: File)
    fun onEdit(file: File)
    fun onDelete(file: File)
}