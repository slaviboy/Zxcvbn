package entities

sealed class Dependency(
    open val path: String,
    open val name: String,
    open val type: DependencyType
) {
    object App : Dependency(":app", "app", DependencyType.Application)

    fun matches(project: org.gradle.api.Project): Boolean {
        return (project.path == path && project.name == name)
    }
}

sealed class LibraryDependency(
    override val path: String,
    override val name: String
) : Dependency(path, name, DependencyType.Library) {

    object Zxcvbn : LibraryDependency(":zxcvbn", "zxcvbn")
}