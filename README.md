# InstantNews

## Required

 - Android SDK 32
 - buildToolsVersion "32.0.0"
 - gradle 7.2
 - Android studio install SDK tools 
   - NDK
   - CMAKE
 - Valid API Key for [Newsapi.org](https://newsapi.org/)

## Getting Started

To build this project
 - Import Project in Android Studio.
 - In app/src/main/cpp/ folder copy file native-lib_template.txt to native-lib.cpp
 - In native-lib.cpp, replace "API_KEY" with your API Key
 - Sync Build gradle
 - Link Gradle to your native library: Build > Refresh Linked C++ Projects

