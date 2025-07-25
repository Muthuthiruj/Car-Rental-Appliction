import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.carrental"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.carrental"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    buildFeatures{
        viewBinding=true
    }
   
    buildFeatures{
        dataBinding=true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.animation.core.android)
    implementation(libs.androidx.animation.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    //lottie_file
    implementation("com.airbnb.android:lottie:6.5.2")

    // CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.14.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:28.4.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")

    implementation ("androidx.recyclerview:recyclerview:1.3.2")

    // Google Play Services
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.gms:play-services-location:18.0.0")

    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.1")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1-Beta")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1-Beta")

    implementation("com.github.denzcoskun:ImageSlideshow:0.1.0")

//Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.4")

//    implementation("com.github.qamarelsafadi:CurvedBottomNavigation:0.1.3")
//
//    implementation ("com.etebarian:meow-bottom-navigation:1.2.0")
//    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.61")

//    implementation("com.exyte:animated-navigation-bar:1.0.0")

    //map
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.libraries.places:places:3.4.0")
    /*   implementation("in.shadowfax:proswipebutton:1.2.0")*/
   /* implementation ("com.ncorti:slidetoact:0.11.0")*/


    implementation ("com.github.ebanx:swipe-button:0.8.3")

    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation ("com.github.skydoves:expandablelayout:1.0.7")

//    implementation ("com.braintreepayments:card-form:3.1.1")


        implementation ("com.github.KunikaValecha:CreditCardView:1.0") // replace with actual latest version

    implementation ("com.android.support:support-core-utils:28.0.0")

    //  Api
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")


    // Update both OkHttp and logging-interceptor to the same version
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0") // Ensure same version





}



