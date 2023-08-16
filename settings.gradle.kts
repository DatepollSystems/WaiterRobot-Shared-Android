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
        from {
            """
                echo "Running detekt check..."
                OUTPUT="/tmp/detekt-${'$'}(date +%s)"
                ./gradlew detekt > ${'$'}OUTPUT
                EXIT_CODE=${'$'}?
                if [ ${'$'}EXIT_CODE -ne 0 ]; then
                  cat ${'$'}OUTPUT
                  rm ${'$'}OUTPUT
                  echo "**********************************************************************************************"
                  echo "                                         detekt failed                                        "
                  echo "                        Please fix the above issues before committing                         "
                  echo " Some of the issues might already be resolved automatically and only must be committed again. "
                  echo "                  Run './gradlew detekt' to to get an updated list of issues.                 "
                  echo "**********************************************************************************************"
                  exit ${'$'}EXIT_CODE
                fi
                rm ${'$'}OUTPUT
            """.trimIndent()
        }
    }
    createHooks(overwriteExisting = true)
}
