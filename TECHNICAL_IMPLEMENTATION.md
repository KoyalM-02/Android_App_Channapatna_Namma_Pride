# Technical Implementation Notes

## Database

The app currently uses Room so it works offline. For the assignment requirement, Firebase should be added as the online source for unique toy IDs and artisan links.

Recommended Firebase collection:

```text
toy_verifications
  284901
    toyName: Classic Rocking Horse
    artisanName: Master Raghu V.
    workshop: Raghu V. Workshop
    artisanImage: artisan_raghu
```

Suggested flow:

1. Keep Room for offline cache.
2. Use Firebase Firestore for online verification records.
3. When a 6 digit toy ID is entered, check Firestore first.
4. Save the result into Room for offline reuse.

Do not enable Firebase Gradle plugins until `google-services.json` is added from your Firebase console.

## UI

This project uses Jetpack Compose. Compose `Card` components are the modern replacement for XML `CardView`.

Used for:

- Toy cards
- Artisan profile cards
- Workshop directory cards
- Verification result cards

## Localization

Kannada resource files were added:

```text
app/src/main/res/values/strings.xml
app/src/main/res/values-kn/strings.xml
```

Next step for full localization is replacing hardcoded `Text("...")` calls with:

```kotlin
Text(stringResource(R.string.verify_my_toy))
```

Then Android automatically shows Kannada when the device/app language is Kannada.

## Success Criteria Mapping

- Verification returns correct artisan for valid ID: implemented with local sample IDs.
- Meet the Maker shows at least 5 workshops: implemented.
- UI is vibrant and toy-like: implemented with red, yellow, teal, rounded cards, and toy images.
- Firebase storage: prepared as the recommended next integration step once Firebase config is available.
