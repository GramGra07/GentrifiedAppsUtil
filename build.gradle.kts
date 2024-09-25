plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.gentrifiedApps.gentrifiedappsutil"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gentrifiedApps.gentrifiedappsutil"
        minSdk = 34
        targetSdk = 34
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation ("org.firstinspires.ftc:Inspection:10.1.0")
    implementation ("org.firstinspires.ftc:Blocks:10.1.0")
    implementation ("org.firstinspires.ftc:RobotCore:10.1.0")
    implementation ("org.firstinspires.ftc:RobotServer:10.1.0")
    implementation ("org.firstinspires.ftc:OnBotJava:10.1.0")
    implementation ("org.firstinspires.ftc:Hardware:10.1.0")
    implementation ("org.firstinspires.ftc:FtcCommon:10.1.0")
    implementation ("org.firstinspires.ftc:Vision:10.1.0")
}