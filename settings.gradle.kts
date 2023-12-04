pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        //Added

        maven { url = uri("https://jitpack.io") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        //added

        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Weather App"
include(":app")
 