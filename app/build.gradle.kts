import deps.Core
import deps.Gateway
import deps.Injection
import deps.Presentation

plugins {
    id("com.android.application")
    id("project-module-plugin")
}

android {
    defaultConfig.applicationId = "com.delbel.bullscows.app"

    viewBinding { isEnabled = true }

    packagingOptions { exclude("META-INF/*.kotlin_module") }
}

dependencies {
    implementation(project(path = ":game:domain"))
    implementation(project(path = ":game:gateway"))
    implementation(project(path = ":game:presentation"))

    implementation(project(path = ":session:domain"))
    implementation(project(path = ":session:gateway"))
    implementation(project(path = ":session:presentation"))

    implementation(Injection.dagger)
    implementation(Injection.daggerSupport)
    implementation(Injection.daggerViewModel)
    kapt(Injection.daggerProcessor)
    kapt(Injection.daggerCompiler)

    implementation(Core.navigationFragment)
    implementation(Core.navigationUi)

    implementation(Gateway.room)

    implementation(Presentation.appCompat)
    implementation(Presentation.material)
    implementation(Presentation.constraintLayout)
}