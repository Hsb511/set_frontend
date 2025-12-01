import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.serialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm()

    val xcfName = "uiKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            implementation(libs.kermit)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlin.serialization.json)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(projects.domain)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        iosMain.dependencies {
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.ktor.client.darwin)
        }
        jvmMain.dependencies {
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

android {
    namespace = "com.team23.data"
    compileSdk = (findProperty("compileSdk") as String).toInt()
    buildFeatures.buildConfig = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }


    val versionMaj = (findProperty("versionMaj") as String).toInt()
    val versionMin = (findProperty("versionMin") as String).toInt()
    val versionFix = (findProperty("versionFix") as String).toInt()
    val appVersionName = "$versionMaj.$versionMin.$versionFix"

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"https://settest.souchefr.synology.me/\"")
            buildConfigField("String", "VERSION_NAME", "\"$appVersionName\"")
        }
        release {
            buildConfigField("String", "BASE_URL", "\"https://set.souchefr.synology.me/\"")
            buildConfigField("String", "VERSION_NAME", "\"$appVersionName\"")
        }
    }
}
