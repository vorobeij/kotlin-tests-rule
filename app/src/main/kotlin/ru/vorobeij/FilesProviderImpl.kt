package ru.vorobeij

import java.io.File

class FilesProviderImpl : FilesProvider {

    override fun getModuleRoots(projectRoot: String): List<String> {
        val modules: MutableList<String> = mutableListOf()
        File(projectRoot).walkTopDown().forEach {
            if (it.name.startsWith("build.gradle")) {
                modules.add(it.parentFile.path)
            }
        }
        return modules
    }

    override fun getTestFiles(
        moduleRoot: String,
        testFilesRelativePath: String
    ): SourceFiles {
        val root = "$moduleRoot/$testFilesRelativePath/kotlin"
        val files: MutableList<String> = mutableListOf()
        File(root)
            .walkTopDown()
            .filter { it.isFile }
            .forEach {
                files.add(it.path.replace(root, ""))
            }
        return SourceFiles(
            root, files
        )
    }

    override fun getSourceFiles(moduleRoot: String): SourceFiles {
        val root = "$moduleRoot/src/main/kotlin"
        val files: MutableList<String> = mutableListOf()
        File(root)
            .walkTopDown()
            .filter { it.isFile }
            .forEach {
                files.add(it.path.replace(root, ""))
            }
        return SourceFiles(
            root, files
        )
    }
}
