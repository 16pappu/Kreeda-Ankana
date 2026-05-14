# Kreeda-Ankana (Android)

Offline-first Sports Ground and Match Organizer for villages.

## Tech Stack

- Kotlin + Jetpack Compose
- Clean Architecture + MVVM + Repository pattern
- Room for offline cache
- Firebase Auth + Firestore for cloud sync
- Hilt for dependency injection

## Modules

- `app`
- `core:common`, `core:model`, `core:database`, `core:network`, `core:data`
- `feature:auth`, `feature:grounds`, `feature:booking`, `feature:challenges`, `feature:scores`, `feature:profile`

## Setup

1. Open project in Android Studio (Giraffe+ recommended).
2. Add Firebase Android app and place `google-services.json` inside `app/`.
3. Enable Firebase Auth (Email/Password + Anonymous) and Firestore.
4. Sync Gradle and run on device/emulator.

## Starter Scope Included

- Modular architecture skeleton
- Booking entity + DAO + repository + Firestore transaction sample
- Hilt app setup + dependency modules
- Basic Compose launch screen and sample booking trigger

## Next Implementation Steps

1. Add full DAOs/repositories for grounds, challenges, replies, scores, profiles.
2. Add WorkManager sync queue and conflict resolution states.
3. Add Navigation graph and feature screens.
4. Add unit, integration, and UI tests from the roadmap.
