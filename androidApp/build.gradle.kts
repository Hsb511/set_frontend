plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.team23.set"
    compileSdk = (findProperty("compileSdk") as String).toInt()

    defaultConfig {
        applicationId = "com.team23.set_app"
        minSdk = (findProperty("minSdk") as String).toInt()
        targetSdk = (findProperty("targetSdk") as String).toInt()

        val versionMaj = (findProperty("versionMaj") as String).toInt()
        val versionMin = (findProperty("versionMin") as String).toInt()
        val versionFix = (findProperty("versionFix") as String).toInt()

        versionCode = versionMaj * 10_000 + versionMin * 100 + versionFix
        versionName = "$versionMaj.$versionMin.$versionFix"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val ksPath = System.getenv("KEYSTORE_PATH")
            val ksPass = System.getenv("KEYSTORE_PASSWORD")
            val alias = System.getenv("KEY_ALIAS")
            val keyPass = System.getenv("KEY_PASSWORD")

            if (ksPath != null && ksPass != null && alias != null && keyPass != null) {
                storeFile = file(ksPath)
                storePassword = ksPass
                keyAlias = alias
                keyPassword = keyPass
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            val ksPath = System.getenv("KEYSTORE_PATH")
            val ksPass = System.getenv("KEYSTORE_PASSWORD")
            val alias = System.getenv("KEY_ALIAS")
            val keyPass = System.getenv("KEY_PASSWORD")

            if (ksPath != null && ksPass != null && alias != null && keyPass != null) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
        getByName("debug") {
            applicationIdSuffix = ".test"
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        compilerOptions {
            jvmToolchain(11)
        }
    }
}

dependencies {
    // Compose
    implementation(compose.components.uiToolingPreview)
    implementation(compose.runtime)

    // Androidx core
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)

    // Koin
    implementation(libs.koin.android)

    // Other modules
    implementation(projects.ui)
    implementation(projects.domain)
    implementation(projects.data)
}
