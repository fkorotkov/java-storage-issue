plugins {
    java
    application
}

repositories {
    jcenter()
}

dependencies {
    implementation("com.google.cloud:google-cloud-storage:1.103.1")
}

application {
    mainClassName = "com.fkorotkov.storage.App"
}
