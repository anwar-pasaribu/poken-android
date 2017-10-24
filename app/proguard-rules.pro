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

# S: Glide
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
# E: Glide

-keepattributes *Annotation*,Signature,Exceptions

