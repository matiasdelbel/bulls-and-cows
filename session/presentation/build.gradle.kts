import deps.Core
import deps.Injection
import deps.UnitTest

plugins {
    id("com.android.library")
    id("project-module-plugin")
}

android { viewBinding { isEnabled = true } }

coverage { excludes("**/di/**", "**/*Screen*", "**/*Adapter*", "**/*Holder*", "**/*MarginItemDecoration*") }

dependencies {
    implementation(project(path = ":session:domain"))
    implementation(project(path = ":game:domain"))

    implementation(Injection.dagger)
    implementation(Injection.daggerSupport)
    implementation(Injection.daggerViewModel)
    kapt(Injection.daggerProcessor)
    kapt(Injection.daggerCompiler)

    implementation(Core.navigationFragment)
    implementation(Core.navigationUi)

    implementation(deps.Presentation.liveData)
    implementation(deps.Presentation.fragment)
    implementation(deps.Presentation.viewModel)
    implementation(deps.Presentation.savedState)

    implementation(deps.Presentation.constraintLayout)
    implementation(deps.Presentation.recyclerView)
    implementation(deps.Presentation.material)

    implementation(deps.Presentation.assistedInjection)
    kapt(deps.Presentation.assistedInjectionCompiler)

    add("testImplementation", UnitTest.coroutines)
    add("testImplementation", UnitTest.core)
}