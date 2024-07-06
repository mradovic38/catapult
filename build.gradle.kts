// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    kotlin("jvm") version "1.9.23"
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.devtools.ksp") version "1.9.23-1.0.20" apply false
    id("androidx.room") version "2.6.1" apply false
    id("com.google.dagger.hilt.android") version "2.51" apply false
}