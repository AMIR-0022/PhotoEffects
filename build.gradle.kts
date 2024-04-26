// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()  // Warning: this repository is going to shut down soon
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
    dependencies {

        classpath(libs.gradle)
        classpath(libs.kotlin.gradle.plugin)

        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}