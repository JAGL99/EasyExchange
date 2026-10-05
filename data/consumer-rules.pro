# ====================================================================
# 1. ATRIBUTOS Y METADATOS GLOBALES
# ====================================================================
# Necesarios para Retrofit, Moshi, Room y Reflection en Kotlin
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes *Annotation*, AnnotationDefault
-keepattributes RuntimeVisibleAnnotations, RuntimeInvisibleAnnotations

# Preservar constructores sintéticos y metadatos de Kotlin
-keep class kotlin.jvm.internal.DefaultConstructorMarker { *; }
-keepclassmembers class * {
    @kotlin.Metadata *;
}

# ====================================================================
# 2. RETROFIT
# ====================================================================
# Conservar llamadas a métodos en interfaces de Retrofit
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# ====================================================================
# 3. MOSHI & DTOs (API DATA LAYER)
# ====================================================================
# Preservar la anotación @Json y sus valores internos (name = "...")
-keep interface com.squareup.moshi.Json {
    public abstract <methods>;
}
-keepclassmembers class * {
    @com.squareup.moshi.Json *;
}

# Clases anotadas con @JsonClass
-keep @com.squareup.moshi.JsonClass class * {
    <fields>;
    <methods>;
}

# Adaptadores generados por Moshi Codegen (*JsonAdapter)
-keep class *JsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
    public <init>(com.squareup.moshi.Moshi, java.lang.reflect.Type[]);
}
-keep class com.jagl.data.api.model.**JsonAdapter { *; }

# Modelos DTO íntegros (campos, métodos y constructores)
-keep class com.jagl.data.api.model.** {
    <fields>;
    <methods>;
    public <init>(...);
    public synthetic <init>(...);
}

# ====================================================================
# 4. DOMAIN LAYER
# ====================================================================
# Mantener modelos de dominio intactos
-keep class com.jagl.domain.model.** {
    *;
}

# ====================================================================
# 5. ROOM & LOCAL DATABASE LAYER
# ====================================================================
# Mantener entidades y el paquete local completo
-keep class com.jagl.data.local.** {
    *;
}

-keep @androidx.room.Entity class * {
    *;
}

# Evitar que se renombren columnas o llaves primarias
-keepclassmembers class * {
    @androidx.room.PrimaryKey <fields>;
    @androidx.room.ColumnInfo <fields>;
}

# Clases de base de datos e implementaciones generadas (_Impl)
-keep class * extends androidx.room.RoomDatabase {
    *;
}
-keep class **_Impl {
    *;
}
-keep class * extends androidx.room.SharedSQLiteStatement {
    public <init>(...);
}

# DAOs de Room y sus métodos anotados
-keep @androidx.room.Dao interface * {
    *;
}
-keepclassmembers interface * {
    @androidx.room.Query *;
    @androidx.room.Insert *;
    @androidx.room.Update *;
    @androidx.room.Delete *;
}

-dontwarn androidx.room.paging.**