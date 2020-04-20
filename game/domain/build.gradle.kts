import deps.Core

plugins {
    id("com.android.library")
    id("project-module-plugin")
}

dependencies {
    implementation(Core.coroutines)
} 