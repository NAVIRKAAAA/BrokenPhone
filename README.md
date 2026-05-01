# Broken Telephone

An Android social game inspired by the classic "telephone" party game. Players alternate between drawing and describing — each person only sees the previous step, not the original, creating a chain of creative misinterpretations.

## The Idea

You know the game "broken telephone"? One person says a word, the next person repeats it, and at
the end it becomes something completely different. This app is the same idea, but with drawings.

One person writes a short phrase. The next person draws it. The next person describes the drawing
with words. And so on. Nobody can see the steps before. At the end, everyone sees the full chain —
and honestly, the result is always either funny or completely unexpected.

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
- **Backend**: Firebase (Firestore, Auth, FCM) + Supabase (image storage)
- **Min SDK**: 27 (Android 8.1)
- **Target SDK**: 37

## Project Structure

```
app/                        # Entry point, AppNavGraph, DI, deep-link handling
core/                       # Shared UI components, theme, utilities
├── theme/                  # Light/dark theme, color tokens, typography
├── button/                 # BTButton, AuthButton, GoogleSignInButton, FAB, etc.
├── badge/                  # BadgeElement, StrongBadgeElement
├── chip/                   # PostChip, UsernameChip
├── avatar/                 # AvatarComponent
├── shimmer/                # Shimmer loading effect
├── dialog/                 # ConfirmDialog, ErrorDialog, LoadingDialog, TimesUpDialog
├── bottom_sheet/           # PostBottomSheet, ReportPostBottomSheet
├── top_bar/                # AuthTopBar, EditProfileTopBar, PostTopBar, ProfileTopBar, SaveTopBar
├── post/                   # DrawPostImage, EmptyDrawPost
├── profile/                # Profile UI (FriendshipActionButton, UserBioDisplay, friend items, shimmer lists)
├── text_field/             # SignUpTextField, PasswordTextField, SearchTextField
├── text/                   # TermsAndPrivacyText
├── pull_to_refresh/        # AppPullToRefreshIndicator
├── divider/                # OrDivider
├── banner/                 # ActiveSessionBanner
├── timer/                  # CountdownTimer
├── pagination/             # LoadMoreIndicator
├── swipe/                  # SwipeToDismissContainer
├── radio_button/           # LanguageRadioItem
├── locale/                 # LocalizedContextWrapper
├── browser/                # CustomTab
└── modifier/               # Custom Modifier extensions (shimmer, hidden, coloredShadow, fadingEdge)

domain/                     # Repository interfaces, use cases, domain models
data/                       # Repository implementations, DTOs, mappers, Firebase/Supabase clients
essentials/                 # AppException hierarchy, SignUpValidator
network/                    # NetworkConnectionManager, NetworkState
nav_api/                    # Navigation route contracts
firebase/                   # Cloud Functions (Node.js/TypeScript)

features/                   # Feature modules (each has a paired <feature>_api module)
├── welcome/
├── sign_in/ + sign_up/
├── confirm_sign_up/
├── forgot_password/ + new_password/
├── choose_avatar/ + choose_username/
├── dashboard/
├── post_details/ + chain_details/
├── create_post/ + draw/ + describe_drawing/
├── profile/ + edit_profile/ + edit_avatar/ + edit_username/ + edit_bio/ + edit_email/
├── settings/ + account_settings/ + blocked_users/
├── language/ + theme/
├── notifications/ + notification_details/ + notifications_settings/
├── friends/ + add_friend/ + user_friends/
├── user_details/
└── bottom_nav_bar/
```

## Getting Started

1. Clone the repository
2. Add your `google-services.json` to `app/`
3. Open in Android Studio (Ladybug or newer recommended)
4. Sync Gradle
5. Run on a device or emulator with API 27+

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
