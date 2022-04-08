# InstantNews

Sample Android Kotlin MVVM application that retrieve news from [Newsapi](https://newsapi.org/)

For the application we use:
- Two Activities for listing and detail
- One ViewModel for fetching the news
- Retrofit library with coroutine for api request
- Glide library for optimizing remote image loading
- CMAKE and NDK for saving securely the API_KEY

There is also some Android unit test impelemented for:
 - Test the layout when loading
 - Test if error view dispay correctly on error
 - Test if the listing display the response
 - Test if the detail of article is open after item click

The Obfuscation and code optmisation are enable for release 


![Screenshot_20220408-180645_InstantNews](https://user-images.githubusercontent.com/13717984/162489593-8336555a-1567-4f24-a05a-fe279e9937a7.jpg)  ![Screenshot_20220408-180728_InstantNews](https://user-images.githubusercontent.com/13717984/162489595-77db4af7-7a70-4aac-9c80-5e212f054d91.jpg)


##Next Step
- [ ] Improve UI with Search bar, history of reading and linkes
- [ ] Save news in local databese with Room
- [ ] Use Jetpack Compose
- [ ] Increase Code Coverage


## Project requirements
 - Android SDK 32
 - buildToolsVersion "32.0.0"
 - gradle 7.2
 - Android studio install SDK tools 
   - NDK
   - CMAKE
 - Valid API Key for [Newsapi](https://newsapi.org/) ( or use this if still valid "e7a5fbfc303745b68b15d49ea9011089" )

## Getting Started
To build this project
 - Import Project in Android Studio.
 - In app/src/main/cpp/ folder copy file native-lib_template.txt to native-lib.cpp
 - In native-lib.cpp, replace "API_KEY" with your API Key
 - Sync Build gradle
 - Link Gradle to your native library: Build > Refresh Linked C++ Projects

