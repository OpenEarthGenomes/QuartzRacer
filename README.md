# QuartzRacer

A QuartzRacer egy ultra-gyors, neon-világú arcade autóversenyzős játék Androidra, Jetpack Compose alapokon. 
A játék ötvözi a 80-as évekbeli "keygen" zenei hangulatot a modern, 2.5D vizuális effektekkel és egy beépített, testreszabható műszerfallal.

## Funkciók
- **2.5D Renderelő Motor:** `Canvas` alapú 3D-s autó-megjelenítés dinamikus perspektívával.
- **Beépített MP3 Lejátszó:** Automatikus keresés a készülék tárolójában, Vice City stílusú futófényes címkijelzéssel.
- **Háromféle Műszerfal:** Vegas Neon, Retro LCD és Track Mode stílusok.
- **Kaszinó-stílusú Játékmenet:** Boost palackok (Piros/Kék), gyalogos-kerülgetés és Near-Miss bónuszok.
- **Késleltetés nélküli Hangeffektek:** `SoundPool` alapú hangeffekt rendszer (fék, boost, jackpot).

## Projektstruktúra
```text
/app/src/main/java/com/example/quartzracer/
├── MainActivity.kt               # Entry point, engedélyek kezelése
├── audio/
│   ├── MusicPlayerManager.kt     # MP3 lejátszó logika
│   └── QuartzAudioManager.kt     # SoundPool hangeffekt vezérlő
├── model/
│   └── GameState.kt              # Játékállapot adatstruktúra
├── ui/components/
│   ├── GameCanvas.kt             # 2.5D grafikus motor
│   └── SpeedometerGauge.kt       # Műszerfal komponens
├── ui/screen/
│   └── GameScreen.kt             # HUD és layout összeállítás
└── viewmodel/
    └── GameViewModel.kt          # Játéklogika és frissítések
