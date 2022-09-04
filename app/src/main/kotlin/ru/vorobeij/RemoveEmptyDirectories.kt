package ru.vorobeij

import java.io.File
import java.nio.file.Files

fun File.isEmptyDirectory(): Boolean {
    if (isFile) {
        return false
    }
    val children = Files.list(this.toPath()).toList()
    if (children.any { it.toFile().isFile }) {
        return false
    }
    if (children.isEmpty()) {
        return true
    }
    if (children.all { it.toFile().isEmptyDirectory() }) {
        return true
    }
    children.forEach {
        if (!it.toFile().isEmptyDirectory()) {
            return false
        }
    }
    return false
}

fun removeEmptyDirectories(root: String) {
    val files: MutableList<File> = mutableListOf()
    File(root).walkTopDown().forEach {
        if (it.isEmptyDirectory()) {
            files.add(it)
        }
    }
    files.reversed().forEach {
        try {
            it.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
