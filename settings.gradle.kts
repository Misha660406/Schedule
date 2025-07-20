pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "Schedule"
include(":app")

include(":shared-date")
project(":shared-date").projectDir = file("shared/date")

include(":shared-group")
project(":shared-group").projectDir = file("shared/group")

include(":shared-schedule")
project(":shared-schedule").projectDir = file("shared/schedule")