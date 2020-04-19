import deps.Core

plugins {
    id("com.android.library")
    id("project-module-plugin")
}

dependencies {
    implementation(project(path = ":game:domain"))

    implementation(Core.coroutines)
} 