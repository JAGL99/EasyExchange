plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jupiter)
}

android {
    namespace = "com.jagl.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        compose = true
    }

    junitPlatform {
        instrumentationTests {
            integrityCheckEnabled = false
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Testing
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.junit.jupiter.api)
    implementation(libs.junit.jupiter.params)
    implementation(libs.androidx.ui.test.junit4)
    implementation(libs.hilt.android.testing)

    testImplementation(libs.junit)
    testImplementation(libs.turbine)
    testImplementation(libs.junit)
    testImplementation(libs.assertk)
    testImplementation(libs.mockk)
    testImplementation(libs.junit5.test.core)

    kspAndroidTest(libs.hilt.android.compiler)
    testRuntimeOnly(libs.junit.jupiter.engine)

    androidTestImplementation (libs.androidx.rules)
    androidTestImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.core.ktx.test)
    androidTestImplementation(libs.assertk)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
}

/**
 * Custom Gradle task
 */
tasks.register("findTodos") {
    group = "verification"
    description = "Detects TODO comments in the codebase."

    doLast {
        val android = project.extensions.getByName("android") as com.android.build.gradle.BaseExtension
        val srcDirs = android.sourceSets["main"].java.srcDirs // Para Kotlin/Java

        val todos = mutableListOf<String>()

        srcDirs.forEach { dir ->
            if (dir.exists()) {
                dir.walk()
                    .filter { it.isFile && (it.extension == "kt" || it.extension == "java") }
                    .forEach { file ->
                        file.forEachLine { line ->
                            if (line.contains("TODO")) {
                                todos.add("${file.relativeTo(dir)}: $line")
                            }
                        }
                    }
            }
        }

        if (todos.isNotEmpty()) {
            println("\n=== TODOs found (${todos.size}) ===")
            todos.forEach { println(it) }
            throw GradleException("Find ${todos.size} TODOs in the code. Please fix them.")
        } else {
            println("¡No TODOs found in the code!")
        }
    }
}