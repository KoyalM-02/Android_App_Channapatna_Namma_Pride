# Namma Pride Android Kotlin App

This folder is the native Android Studio conversion of the HTML screens in the parent project.

## How to run

1. Open Android Studio.
2. Choose **Open**.
3. Select this folder:
   `stitch_channapatna_namma_pride/android_kotlin_app`
4. Let Android Studio sync Gradle.
5. Run the `app` configuration on an emulator or Android phone.

## What was converted

- Splash screen
- Role selection
- OTP verification
- Customer bazaar
- Product detail story page
- Meet the maker map
- Wishlist / saved toys
- User settings profile
- Artisan profile
- Artisan dashboard
- Add new toy form

The app is written in Kotlin using Jetpack Compose. Screens are native Android UI, scrollable where needed, and navigation is handled with simple screen state inside `MainActivity.kt`.

## Main file

`app/src/main/java/com/nammapride/channapatna/MainActivity.kt`

## Technical notes

See `TECHNICAL_IMPLEMENTATION.md` for Firebase database structure, Card UI notes, Kannada localization files, and success-criteria mapping.
