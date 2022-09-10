package ru.vorobeij

import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class PackageStructure(
    private val arguments: Arguments,
    private val filesProvider: FilesProvider = FilesProviderImpl()
) {

    private val undefinedFolder = File(arguments.projectRoot, "undefinedTests")

    init {
        undefinedFolder.mkdirs()
    }

    fun moveTestSourcesFiles() {

        val result = StringBuilder()
        filesProvider.getModuleRoots(arguments.projectRoot).forEach { moduleRoot ->
            val sourceFiles = filesProvider.getSourceFiles(moduleRoot)
            listOf(
                "src/test",
                "src/androidTest"
            ).forEach { testRelativeDir ->
                visitModule(filesProvider.getTestFiles(moduleRoot, testRelativeDir), sourceFiles, result)
            }
        }
        if (result.toString().isNotEmpty()) {
            throw Exception("Test source sets are not mirrored with code source sets. Run kotlin-tests-rule.jar locally")
        }
    }

    private fun visitModule(
        testFiles: SourceFiles,
        sourceFiles: SourceFiles,
        result: java.lang.StringBuilder
    ) {

        testFiles.files.forEach { testFilePath ->

            if (!sourceFiles.files.contains(testFilePath.replace("Test.kt", ".kt"))) {

                val name = File(testFiles.root, testFilePath).name.replace("Test.kt", ".kt")
                val sourceFile = sourceFiles.files.find { File(sourceFiles.root, it).name == name }

                val targetFile = sourceFile?.let {
                    val newName = File(it).name.replace("(Test)?.kt".toRegex(), "Test.kt")
                    File(testFiles.root, File(File(it).parentFile, newName).path)
                } ?: run {
                    File(undefinedFolder, testFilePath)
                }
                targetFile.parentFile.mkdirs()
                result.append("${File(testFiles.root, testFilePath).toPath()} -> ${targetFile.toPath()}\n")
                Files.move(File(testFiles.root, testFilePath).toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
        }

        removeEmptyDirectories(testFiles.root)
    }
}
