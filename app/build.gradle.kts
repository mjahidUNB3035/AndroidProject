plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "ca.unb.mobiledev.weatherapp"
    compileSdk = 33

    //Binding
    buildFeatures {
        viewBinding = true
    }
    buildFeatures {
        dataBinding = true
    }

    defaultConfig {
        applicationId = "ca.unb.mobiledev.weatherapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //Lottie Animation 6.1.0
    //implementation ("com.airbnb.android:lottie:latest_version")
    implementation ("com.airbnb.android:lottie:3.7.0")

    //implementation ("com.airbnb.android:lottie:6.1.0")
    //implementation ("com.squareup.retrofit2:converter-gson:latest.version")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    //Location
    implementation ("com.google.android.gms:play-services-location:18.0.0")

    //real time loc acces dependenct
    implementation ("com.google.android.gms:play-services-location:<latest_version>'")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //MAX MIN TEMP
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    //line chart
    //implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")



    //implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

}