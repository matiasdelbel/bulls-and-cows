import deps.Core
import deps.Injection
import deps.Gateway
import deps.UnitTest

plugins {
    id("com.android.library")
    id("project-module-plugin")
}

dependencies {
    implementation(project(path = ":game:domain"))

    implementation(Injection.dagger)
    kapt(Injection.daggerCompiler)

    implementation(Core.coroutines)

    implementation(Gateway.room)
    kapt(Gateway.roomCompiler)

    add("testImplementation", UnitTest.coroutines)
} 