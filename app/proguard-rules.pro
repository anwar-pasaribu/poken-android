# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/marzellaalfamega/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# S: RETOROFIT/OKHTTP STUFF
-dontwarn okio.**
-dontwarn com.squareup.picasso.**
-dontwarn retrofit2.Platform$Java8
# E: RETOROFIT STUFF

# S: Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
-keep class id.unware.poken.pojo.** { *; }
-keep class id.unware.poken.domain.** { *; }
# E: Application classes that will be serialized/deserialized over Gson

# S: SearchView crash on release version
-keep class android.support.v7.widget.SearchView { *; }
# E: SearchView crash on release version

