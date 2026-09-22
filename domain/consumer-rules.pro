# Keep constructor parameters with defaults intact
-keepclassmembers class com.jagl.domain.model.** {
    public synthetic <init>(...);
    public <init>(...);
}