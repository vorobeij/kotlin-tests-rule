plugins {
    kotlin("jvm")
    `maven-publish`
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(KotlinX.cli)
}

// https://github.com/autonomousapps/dependency-analysis-android-gradle-plugin/wiki/Customizing-plugin-behavior
dependencyAnalysis {
    issues {
        ignoreKtx(true)
        onAny {
            severity("fail")
            exclude(
                "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
            )
        }
    }
}

apply(from = "$rootDir/jacoco.gradle")

// todo to separate script
tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "ru.vorobeij.Application"
    }
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from({
        configurations.runtimeClasspath.get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    })
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "ru.vorobeij"
            artifactId = "kotlin.tests.rule"
            version = "1.0"
            from(components["java"])
        }
    }
}