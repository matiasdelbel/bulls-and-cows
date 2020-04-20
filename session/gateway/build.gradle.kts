import deps.Injection
import deps.Core
import deps.Gateway
import deps.UnitTest

plugins {
    id("com.android.library")
    id("project-module-plugin")
}

coverage { excludes("**/di/**", "**/database/**") }

dependencies {
    implementation(project(path = ":session:domain"))
    implementation(project(path = ":game:domain"))

    implementation(Injection.dagger)
    kapt(Injection.daggerCompiler)

    implementation(Core.coroutines)

    implementation(Gateway.room)
    kapt(Gateway.roomCompiler)

    add("testImplementation", UnitTest.coroutines)
} 