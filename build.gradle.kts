plugins {
    kotlin("multiplatform") version "1.4.20"
}

group = "me.tobiasfried"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native") {
            compilations["main"].enableEndorsedLibs = true
        }
        hostOs == "Linux" -> linuxX64("native") {
            compilations["main"].enableEndorsedLibs = true
        }
        isMingwX64 -> mingwX64("native") {
            compilations["main"].enableEndorsedLibs = true
        }
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }
    sourceSets {
        val nativeMain by getting
        val nativeTest by getting
    }
}
