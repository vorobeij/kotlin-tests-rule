package ru.vorobeij

interface FilesProvider {

    fun getModuleRoots(projectRoot: String): List<String>

    /**
     * src/test|/**/*.kt
     *
     * @param moduleRoot
     * @param testFilesRelativePath
     * @return List of paths to test files in module
     */
    fun getTestFiles(
        moduleRoot: String,
        testFilesRelativePath: String = "src/test"
    ): SourceFiles

    /**
     * src/[!test]/**/*.kt
     *
     * @param moduleRoot
     * @return List of paths to source files in module
     */
    fun getSourceFiles(moduleRoot: String): SourceFiles
}
