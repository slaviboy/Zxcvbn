import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.kapt(vararg list: String) {
    list.forEach { dependency ->
        add("kapt", dependency)
    }
}

fun DependencyHandler.ksp(vararg list: String) {
    list.forEach { dependency ->
        add("ksp", dependency)
    }
}

fun DependencyHandler.implementations(vararg list: String) {
    list.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.projects(vararg list: String) {
    list.forEach { dependency ->
        add("implementation", project(mapOf("path" to dependency)))
    }
}

fun DependencyHandler.platforms(vararg list: String) {
    list.forEach { dependency ->
        add("implementation", platform(dependency))
    }
}

fun DependencyHandler.androidTestImplementations(vararg list: String) {
    list.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.testImplementations(vararg list: String) {
    list.forEach { dependency ->
        add("testImplementation", dependency)
    }
}