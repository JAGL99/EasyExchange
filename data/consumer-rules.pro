# Preserve generic signatures for Retrofit and Moshi reflection
-keepattributes Signature
-keepattributes *Annotation*, InnerClasses, EnclosingMethod

# Keep Moshi generated adapters and annotations
-keepclassmembers class * {
    @com.squareup.moshi.Json *;
}
-keep class *JsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
    public <init>(com.squareup.moshi.Moshi, java.lang.reflect.Type[]);
}
-keepnames class * {
    @com.squareup.moshi.JsonClass *;
}

# Keep Retrofit interface methods and return types intact
-keepattributes EnclosingMethod, InnerClasses
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Preserve Kotlin synthetic default constructor markers
-keep class kotlin.jvm.internal.DefaultConstructorMarker { *; }

# Keep constructor parameters with defaults intact
-keepclassmembers class com.jagl.data.api.model.** {
    public synthetic <init>(...);
    public <init>(...);
}

# Preserve Kotlin Metadata (critical if kotlin-reflect is used)
-keepclassmembers class * {
    @kotlin.Metadata *;
}
-keepattributes RuntimeVisibleAnnotations, RuntimeInvisibleAnnotations

# Ensure Moshi generated adapters are kept and not obfuscated
-keep class com.jagl.data.api.model.**JsonAdapter {
    public <init>(...);
}