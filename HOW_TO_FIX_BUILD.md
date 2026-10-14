# Agar build fail ho

## Common fixes (already applied in this zip)

1. **ViewModel naming conflict** – `addBudget` / `addGoal` recursive call fixed
2. **MaterialSwitch** → replaced with `SwitchMaterial` (more compatible)
3. **coordinatorlayout** dependency added
4. **Gradle wrapper properties** added (Gradle 8.2)
5. **WorkManager + Hilt** init made safer

## Android Studio steps

1. **File → Invalidate Caches → Invalidate and Restart**
2. **File → Sync Project with Gradle Files**
3. Agar wrapper missing: right-click project → **Gradle → Wrapper → Generate**
4. SDK: ensure **Android SDK 34** and **Build-Tools** installed
5. JDK: use **JDK 17** (File → Settings → Build → Gradle → Gradle JDK)

## Error paste karein

Agar phir bhi fail ho to **Build** tab ka full error yahan paste karein:
- `e: file://...` wali lines
- ya `FAILURE: Build failed with an exception` block
