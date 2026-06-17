#==============================================================================
# QuartzRacer - Végleges R8 / ProGuard Konfigurációs Fájl
#==============================================================================

# 1. Általános Android és Java attribútumok megőrzése a stabilitásért
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod, SourceFile, LineNumberTable

# 2. Jetpack Compose specifikus védelmi szabályok
# Biztosítja, hogy a Compose fordító és a recomposition (újrarajzolás) mechanizmus ne sérüljön meg.
-keepclassmembers class * {
    @androidx.compose.runtime.Composable class *;
    @androidx.compose.runtime.ReadOnlyComposable class *;
}

# 3. A GameViewModel és annak konstruktorainak teljes megőrzése
# Ez elengedhetetlen, mert a 'viewModel()' delegált tükrözéssel (reflection) példányosítja az osztályt.
-keep class com.example.quartzracer.viewmodel.GameViewModel {
    public <init>(android.app.Application);
}
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}

# 4. A Játék Adatmodelljeinek megvédése (GameState, GameEntity, EntityType, GaugeStyle, Particle)
# Megakadályozza, hogy az obfuscator átnevezze a mezőket, így a hibakeresés és az adatok konzisztensek maradnak.
-keep class com.example.quartzracer.model.** { *; }
-keep class com.example.quartzracer.model.EntityType { *; }
-keep class com.example.quartzracer.model.GaugeStyle { *; }

# 5. Kotlin Coroutines és Flow aszinkron motor védelme
# Megakadályozza, hogy a belső állapotgépek (StateFlow) optimalizáció áldozatává váljanak a játékloop futása közben.
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keep class kotlinx.coroutines.CoroutineExceptionHandler { *; }
-keep class kotlinx.coroutines.android.AndroidDispatcherFactory { *; }

# 6. Android Runtime és Eszközkezelő engedélyek megőrzése
# Szükséges a MediaStore és a SoundPool hangeffektusok zökkenőmentes eléréséhez.
-keep class android.media.SoundPool { *; }
-keep class android.media.MediaPlayer { *; }

# 7. Release Build optimalizáció: Log.d és Log.v (hibakeresési naplók) automatikus kiszűrése
# Ez növeli a játék futási sebességét és csökkenti az APK méretét azáltal, hogy eltávolítja a felesleges szöveges logokat.
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
}

