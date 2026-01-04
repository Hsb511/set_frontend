import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.serialization)
    alias(libs.plugins.buildkonfig)
}

buildkonfig {
    packageName = "com.team23.data"

    val versionMaj = (findProperty("versionMaj") as String).toInt()
    val versionMin = (findProperty("versionMin") as String).toInt()
    val versionFix = (findProperty("versionFix") as String).toInt()
    val appVersionName = "$versionMaj.$versionMin.$versionFix"


    val requestedTasks = gradle.startParameter.taskNames.joinToString(" ")
    val isReleaseBuild = requestedTasks.contains("Release", ignoreCase = true)
    val baseUrl = if (isReleaseBuild) "https://set.souchefr.synology.me/" else "https://settest.souchefr.synology.me/"

    defaultConfigs {
        buildConfigField(STRING, "VERSION_NAME", appVersionName)
        buildConfigField(STRING, "BASE_URL", baseUrl)
    }
}

kotlin {
    jvmToolchain(11)

    androidLibrary {
        namespace = "com.team23.data"
        compileSdk = (findProperty("compileSdk") as String).toInt()

        androidResources {
            enable = true
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
