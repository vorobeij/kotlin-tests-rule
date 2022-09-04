package ru.vorobeij

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required

/**
 * @property projectRoot
 */
data class Arguments(
    val projectRoot: String
) {

    companion object {

        fun fromArgs(args: Array<String>): Arguments {
            val parser = ArgParser("kotlin-tests-rule")
            val projectRoot by parser.option(ArgType.String, description = "Project root path").required()
            parser.parse(args)

            return Arguments(
                projectRoot = projectRoot
            )
        }
    }
}
