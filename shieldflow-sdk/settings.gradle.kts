rootProject.name = "shieldflow-android-sdk"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.projectDir.listFiles()?.let { files ->
    files.filter { file ->
        file.isDirectory && File(file, "build.gradle.kts").exists()
    }.forEach { include(":${it.name}") }
}
