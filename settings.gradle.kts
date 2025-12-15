import java.io.File

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

val configuredAndroidSdk = System.getenv("ANDROID_SDK_ROOT") ?: System.getenv("ANDROID_HOME")
if (configuredAndroidSdk == null) {
    val candidates = listOf(
        File("/opt/android-sdk"),
        File("/usr/lib/android-sdk"),
        File(System.getProperty("user.home"), "Android/Sdk"),
    )

    val sdkDir = candidates.firstOrNull { it.exists() }
    if (sdkDir != null) {
        val localProperties = File(rootDir, "local.properties")
        if (!localProperties.exists()) {
            localProperties.writeText("sdk.dir=${sdkDir.absolutePath}\n")
        }
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "instantcopy"
include(":shared", ":android")
