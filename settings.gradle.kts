pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Kreeda-Ankana"

include(
    ":app",
    ":core:common",
    ":core:model",
    ":core:database",
    ":core:network",
    ":core:data",
    ":feature:auth",
    ":feature:grounds",
    ":feature:booking",
    ":feature:challenges",
    ":feature:scores",
    ":feature:profile"
)
