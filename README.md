# Broken Telephone

An Android social game inspired by the classic "telephone" party game. Players alternate between drawing and describing — each person only sees the previous step, not the original, creating a chain of creative misinterpretations.

## How It Works

1. A user starts a chain by writing a text prompt
2. The next player draws it
3. The next player describes the drawing in text
4. Repeat until the chain is complete
5. Everyone can see how the original idea transformed

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material3
- **Architecture**: MVVM + Use Cases
- **DI**: Koin
- **Navigation**: Navigation Compose
- **Image loading**: Coil
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36

## Project Structure

```
app/src/main/java/com/broken/telephone/
├── core/               # Shared UI components, theme, utilities
│   ├── theme/          # Light/dark theme, color tokens, typography
│   ├── badge/          # Badge components
│   ├── button/         # Auth button
│   ├── avatar/         # Avatar component
│   ├── shimmer/        # Shimmer loading effect
│   ├── dialog/         # Reusable dialogs
│   ├── bottom_sheet/   # Bottom sheets (post actions, report)
│   ├── top_bar/        # Shared top bars
│   ├── post/           # Post image rendering
│   └── modifier/       # Custom Modifier extensions
├── features/           # Feature modules
│   ├── welcome/
│   ├── sign_in/
│   ├── sign_up/
│   ├── dashboard/
│   ├── post_details/
│   ├── chain_details/
│   ├── create_post/
│   ├── draw/
│   ├── describe_drawing/
│   ├── profile/
│   ├── edit_profile/
│   ├── edit_avatar/
│   ├── edit_username/
│   ├── settings/
│   ├── account_settings/
│   ├── blocked_users/
│   ├── language/
│   ├── theme/
│   ├── notifications/
│   └── bottom_nav_bar/
├── navigation/         # Nav graph and routes
├── domain/             # Domain models and repository interfaces
└── data/               # Mock repository implementations
```

## Getting Started

1. Clone the repository
2. Open in Android Studio (Ladybug or newer recommended)
3. Sync Gradle
4. Run on a device or emulator with API 24+

## Building

```bash
./gradlew assembleDebug
```

## Testing

Screenshot tests with Paparazzi:

```bash
./gradlew verifyPaparazziDebug   # verify against golden images
./gradlew recordPaparazziDebug   # record new golden images
```

## Theme

The app supports light and dark themes via `BrokenTelephoneTheme`. Theme selection is user-controlled through the Theme settings screen. Custom colors beyond Material3 tokens are provided through `MaterialTheme.appColors`.
