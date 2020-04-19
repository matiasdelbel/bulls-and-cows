import deps.Injection
import deps.Core
import deps.Presentation
import deps.UnitTest

plugins {
    id("com.android.library")
    id("project-module-plugin")
}

android { viewBinding { isEnabled = true } }

dependencies {
    implementation(project(path = ":game:domain"))

    implementation(Injection.dagger)
    implementation(Injection.daggerSupport)
    implementation(Injection.daggerViewModel)
    kapt(Injection.daggerProcessor)
    kapt(Injection.daggerCompiler)

    implementation(Core.navigationFragment)
    implementation(Core.navigationUi)

    implementation(Presentation.liveData)
    implementation(Presentation.fragment)
    implementation(Presentation.viewModel)
    implementation(Presentation.savedState)

    implementation(Presentation.constraintLayout)
    implementation(Presentation.material)

    implementation(Presentation.assistedInjection)
    kapt(Presentation.assistedInjectionCompiler)

    add("testImplementation", UnitTest.coroutines)
    add("testImplementation", UnitTest.core)
} 