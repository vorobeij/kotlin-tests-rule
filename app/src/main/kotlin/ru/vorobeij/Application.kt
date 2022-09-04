package ru.vorobeij

@Suppress("DEBUG_PRINT")
object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        val arguments = Arguments.fromArgs(args)
        println("Arguments = $arguments")
        PackageStructure(arguments).moveTestSourcesFiles()
    }
}
