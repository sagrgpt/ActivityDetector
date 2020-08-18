object Versions {
    const val minSdkVersion = 22
    const val targetSdkVersion = 29
    const val kotlin = "1.3.72"
    const val gradle = "4.0.0"
    const val core_ktx = "1.3.0"
    const val app_compat = "1.1.0"
    const val constraint_layout = "1.1.3"
    const val recycler_view = app_compat
    const val dagger = "2.28"
    const val rx_android = "3.0.0"
    const val rx_java = "3.0.4"
    const val timber = "4.7.1"
    const val google_service = "4.3.3"
    const val firebase_analytics = "17.2.2"
    const val firebase_crash = "17.1.0"
    const val crashlytics_plugin = "2.2.0"
    const val tensor_flow_version = "1.13.1"
}

object TestVersion {
    const val junit = "4.12"
    const val mockito_core = "3.2.4"
    const val mockito_kotlin = "1.6.0"
    const val google_truth = "0.42"
    const val android_arch_core = "1.1.1"
}

object Dep {
    const val gradle_build = "com.android.tools.build:gradle:${Versions.gradle}"
    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val kotlin_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val android_core_ktx = "androidx.core:core-ktx:${Versions.core_ktx}"
    const val androidx_app_compat = "androidx.appcompat:appcompat:${Versions.app_compat}"
    const val androidx_constraint_layout = "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}"
    const val androidx_recycler_view = "androidx.recyclerview:recyclerview:${Versions.recycler_view}"
    const val dagger_core = "com.google.dagger:dagger:${Versions.dagger}"
    const val dagger_compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    const val rx_android = "io.reactivex.rxjava3:rxandroid:${Versions.rx_android}"
    const val rx_java = "io.reactivex.rxjava3:rxjava:${Versions.rx_java}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val google_services = "com.google.gms:google-services:${Versions.google_service}"
    const val firebase_analytics = "com.google.firebase:firebase-analytics:${Versions.firebase_analytics}"
    const val firebase_crashlytics = "com.google.firebase:firebase-crashlytics:${Versions.firebase_crash}"
    const val crashlytics_gradle_plugin = "com.google.firebase:firebase-crashlytics-gradle:${Versions.crashlytics_plugin}"
    const val tensor_flow_lite = "org.tensorflow:tensorflow-lite:${Versions.tensor_flow_version}"
}

object TestDep {
    const val junit = "junit:junit:${TestVersion.junit}"
    const val mockito_core = "org.mockito:mockito-core:${TestVersion.mockito_core}"
    const val mockito_kotlin = "com.nhaarman:mockito-kotlin:${TestVersion.mockito_kotlin}"
    const val google_truth = "com.google.truth:truth:${TestVersion.google_truth}"
    const val architecture_core = "android.arch.core:core-testing:${TestVersion.android_arch_core}"
}