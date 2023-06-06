object ApplicationDependencies {

    // with versions
    private const val core = "androidx.core:core-ktx:${ApplicationDependencyVersions.core}"
    private const val activity = "androidx.activity:activity-compose:${ApplicationDependencyVersions.activity}"
    private const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${ApplicationDependencyVersions.lifecycle}"
    private const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${ApplicationDependencyVersions.lifecycle}"
    private const val kotlinStandardLib = "org.jetbrains.kotlin:kotlin-stdlib:${ApplicationDependencyVersions.kotlin}"
    private const val kotlinxSerializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:${ApplicationDependencyVersions.kotlinxSerializationJson}"
    private const val kotlinxCoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${ApplicationDependencyVersions.kotlinCoroutinesCore}"

    // Compose BOM no versions
    private const val composeUi = "androidx.compose.ui:ui"
    private const val composeUiGraphics = "androidx.compose.ui:ui-graphics"
    private const val composeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
    private const val composeUiMaterial3 = "androidx.compose.material3:material3"
    const val composeBom = "androidx.compose:compose-bom:${ApplicationDependencyVersions.composeBom}"

    // Testing
    private const val junit = "junit:junit:${ApplicationDependencyVersions.junit}"
    private const val truth = "com.google.truth:truth:${ApplicationDependencyVersions.truth}"
    private const val testCore = "androidx.test:core-ktx:${ApplicationDependencyVersions.testCore}"
    private const val testExtJunit = "androidx.test.ext:junit-ktx:${ApplicationDependencyVersions.textExtJunit}"
    private const val robolectric = "org.robolectric:robolectric:${ApplicationDependencyVersions.robolectric}"
    private const val rhino = "io.apisense:rhino-android:1.0"

    val dependenciesApp = arrayOf(
        core,
        activity,
        lifecycle,
        lifecycleViewModel,
        kotlinStandardLib,
        kotlinxSerializationJson,
        kotlinxCoroutinesCore,
        composeUi,
        composeUiGraphics,
        composeUiToolingPreview,
        composeUiMaterial3
    )

    val dependenciesLibrary = arrayOf(
        core
    )

    val dependenciesTest = arrayOf(
        junit,
        truth,
        testCore,
        testExtJunit,
        robolectric,
        rhino
    )
}