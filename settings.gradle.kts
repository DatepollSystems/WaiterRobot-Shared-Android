pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "WaiterRobot"
include(":androidApp")
include(":shared")

plugins {
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "1.1.9"
}

gitHooks {
    preCommit {
        tasks("detekt")
    }
    createHooks(overwriteExisting = true)
}
