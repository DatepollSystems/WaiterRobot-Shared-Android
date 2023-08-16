import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.report.ReportMergeTask

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        val kotlinVersion = "1.9.0"
        classpath("com.android.tools.build:gradle:8.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${kotlinVersion}")
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt").version("1.23.1")
}

val detektReportMergeSarif by tasks.registering(ReportMergeTask::class) {
    output = layout.buildDirectory.file("reports/detekt/merge.sarif")
}


allprojects {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    apply(plugin = "io.gitlab.arturbosch.detekt")

    detekt {
        config.from(rootDir.resolve("detekt.yml"))
        buildUponDefaultConfig = true
        basePath = rootDir.path
        autoCorrect = true
    }

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.1")
    }

    tasks.withType<Detekt>().configureEach {
        reports {
            html.required = true
            sarif.required = true
        }
        finalizedBy(detektReportMergeSarif)
        autoCorrect = true
    }
    detektReportMergeSarif {
        input.from(tasks.withType<Detekt>().map { it.sarifReportFile })
    }
}
