plugins {
    id("module-convention")
}

dependencies {
    api(projects.core)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "shieldflow-android-sdk"
            from(components.getByName("java"))
        }
    }
}
