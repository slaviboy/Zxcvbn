plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = ApplicationConfiguration.appNamespace
    compileSdk = ApplicationConfiguration.compileSdk
    defaultConfig {
        applicationId = ApplicationConfiguration.appNamespace
        minSdk = ApplicationConfiguration.minSdk
        targetSdk = ApplicationConfiguration.targetSdk
        versionCode = ApplicationConfiguration.versionCode
        versionName = ApplicationConfiguration.versionName
        testInstrumentationRunner = ApplicationConfiguration.androidTestInstrumentation
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
}

dependencies {
    platforms(ApplicationDependencies.composeBom)
    implementations(*ApplicationDependencies.dependenciesApp)
    projects(":zxcvbn")
}
