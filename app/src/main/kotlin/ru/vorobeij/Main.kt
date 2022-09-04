package ru.vorobeij

import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@Suppress("DEBUG_PRINT")
object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val arguments = Arguments.fromArgs(args)

        println("Arguments = $arguments")

        val filesProvider: FilesProvider = FilesProviderImpl()
        val undefinedFolder = File(arguments.projectRoot, "undefinedTests")
        undefinedFolder.mkdirs()

        val result = StringBuilder()
        filesProvider.getModuleRoots(arguments.projectRoot).forEach { moduleRoot ->
            val testFiles = filesProvider.getTestFiles(moduleRoot)
            val sourceFiles = filesProvider.getSourceFiles(moduleRoot)
            testFiles.files.forEach { testFilePath ->

                if (!sourceFiles.files.contains(testFilePath.replace("Test.kt", ".kt"))) {

                    val name = File(testFiles.root, testFilePath).name.replace("Test.kt", ".kt")
                    val sourceFile = sourceFiles.files.find { File(sourceFiles.root, it).name == name }

                    sourceFile?.let {
                        val targetFile = File(testFiles.root, sourceFile)
                        targetFile.parentFile.mkdirs()
                        result.append("${File(testFiles.root, testFilePath).path} -> ${targetFile.path}\n")
                        Files.move(File(testFiles.root, testFilePath).toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                    } ?: run {
                        val targetFile = File(undefinedFolder, testFilePath)
                        targetFile.parentFile.mkdirs()
                        result.append("${File(testFiles.root, testFilePath).toPath()} -> ${targetFile.toPath()}\n")
                        Files.move(File(testFiles.root, testFilePath).toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                    }
                }
            }

            removeEmptyDirectories(testFiles.root)
        }
        if (result.toString().isNotEmpty()) {
            throw Exception("Test source sets are not mirrored with code source sets. Run kotlin-tests-rule.jar locally")
        }
    }
}
