# FitNest — Phase 1 MVP

Kotlin + Jetpack Compose Android app implementing the FitNest Phase 1 MVP:
personal profile, home dashboard, food/nutrition logging with an estimate-and-confirm
flow, water tracking, a built-in home-workout library with a step-by-step player,
progress tracking (weight, workout count, logging streak), and a rule-based NestAI
chat screen you can later swap for a real AI backend.

Data is stored **locally on-device with Room** — no Firebase project or API keys
are required to build and run it. This matches the original spec's tech stack
(Kotlin, Jetpack Compose) while keeping the MVP runnable immediately.

## Run it

1. Install Android Studio (Koala or newer).
2. `File > Open` this folder.
3. Let Gradle sync (Android Studio will generate the Gradle wrapper automatically
   the first time you sync, or run `gradle wrapper` if you have Gradle installed).
4. Run on an emulator or device with API 26+.

## Push to GitHub

```bash
git init
git add .
git commit -m "FitNest Phase 1 MVP"
git branch -M main
git remote add origin https://github.com/<your-username>/FitNest.git
git push -u origin main
```

## What's implemented (Phase 1)

- Profile setup (name, age, height, weight, activity level, goal) → stored in Room
- Home dashboard: nutrition/water/workout summary cards + quick actions
- Food logging: free-text entry → estimated calories/macros → confirm before saving
- Water tracking: quick-add buttons, daily goal progress
- Home workouts: 5 starter workouts across Full Body / Core / Upper / Lower / Mobility,
  each with a step-by-step in-app player and rest info
- Progress: workouts completed, days logged, weight history entry
- NestAI: simple rule-based chat screen (see `ui/ai/AiScreen.kt`) that responds to
  time-available, food, water and progress questions, and declines medical questions

## What's intentionally stubbed / next steps (Phase 2+)

- **Firebase Authentication + Firestore sync** — swap `FitNestRepository`'s Room calls
  for Firestore calls; the UI layer doesn't need to change since it only talks to
  the repository.
- **Real AI backend for NestAI** — replace `replyTo()` in `AiScreen.kt` with a call to
  your own backend, which should hold the Anthropic/OpenAI API key server-side
  rather than embedding it in the app (per the original spec's security note).
- **Reminders** (water/workout/meal notifications) — use `AlarmManager` or
  `WorkManager` plus a notification channel.
- **Charts** on the Progress screen — the data (weight history, workout logs) is
  already exposed as Flows; add a chart library (e.g. Vico) to visualize it.
- **Premium/freemium gating** — add a `isPremium` flag to `UserProfile` and gate
  screens/features on it.

## Project structure

```
app/src/main/java/com/fitnest/app/
  data/models/      Room entities + Workout library data classes
  data/db/           Room DAOs + AppDatabase
  data/repository/   FitNestRepository (swap backend here later) + WorkoutLibrary
  ui/theme/          Compose theme/colors
  ui/nav/            AppViewModel + NavGraph (bottom nav: Home/Food/Workout/Progress/AI)
  ui/home/           Home dashboard
  ui/profile/        First-run profile setup
  ui/food/           Food & nutrition tracker
  ui/workout/        Workout library + step-by-step player
  ui/progress/       Progress tracking
  ui/ai/             NestAI chat (rule-based placeholder)
```
