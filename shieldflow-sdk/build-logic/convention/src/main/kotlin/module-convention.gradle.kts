plugins {
    `java-library`
    kotlin("jvm")
    id("maven-publish")
}

group = "com.sorgeligt.shieldflow"
version = libs.versions.shieldflowVersion.get()

kotlin {
    explicitApi()
}

dependencies {
    compileOnly(findAndroidJar(targetSdk))
    compileOnly(files("../libs/androidx-fragment-1.6.2.jar"))
    compileOnly(files("../libs/androidx-activity-1.8.1.jar"))
    compileOnly(files("../libs/androidx-lifecycle-viewmodel-2.6.2.jar"))
    compileOnly(files("../libs/androidx-savedstate-1.2.1.jar"))
    compileOnly(files("../libs/androidx-core-1.12.0.jar"))
    compileOnly(libs.androidLifecycleCommon)
    compileOnly(libs.okhttp3)
    compileOnly(libs.retrofit)
    compileOnly(libs.retrofitGson)
    testImplementation(kotlin("test"))
}
