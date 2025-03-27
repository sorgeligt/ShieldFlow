plugins {
    id("module-convention")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "shieldflow-core-android-sdk"
            from(components.getByName("java"))
        }
    }
}
