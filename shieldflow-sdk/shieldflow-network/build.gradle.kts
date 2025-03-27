plugins {
    id("module-convention")
}

dependencies {
    api(projects.core)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "shieldflow-network-android-sdk"
            from(components.getByName("java"))
        }
    }
}
