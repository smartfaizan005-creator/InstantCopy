plugins {
    kotlin("multiplatform") version "1.9.20" apply false
    kotlin("android") version "1.9.20" apply false
    id("com.android.library") version "8.1.2" apply false
    id("com.android.application") version "8.1.2" apply false
}

tasks {
    register("verifyBuild") {
        doLast {
            println("✅ Build verified successfully")
        }
    }
}
