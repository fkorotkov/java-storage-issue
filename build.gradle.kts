plugins {
    java
    application
}

repositories {
    jcenter()
}

dependencies {
    implementation("com.google.cloud:google-cloud-storage:1.103.2")
}

application {
    mainClassName = "com.fkorotkov.storage.App"
}
