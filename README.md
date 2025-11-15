# Solar Explorer

A Jetpack Compose Android app that teaches about the Solar System with animations and narrated explanations.

---

## Demo
![Home](screens/home.png) ![Detail](screens/detail.png)

---

## Features
- Animated Lottie solar-system banner
- Planet detail screens with images & Text-to-Speech (TTS)
- Per-planet quiz & scoring
- Guided TTS Tour (plays planet descriptions in sequence)
- Persistent settings & high-scores (DataStore)
- Jetpack Compose + MVVM architecture

---

## Project structure (main files)
- `app/src/main/java/com/example/solarexplorer/ui/` — Compose screens (Home, Detail, Quiz, Tour, Settings)
- `app/src/main/java/com/example/solarexplorer/data/` — `model/`, `repository/`, `datastore/`
- `app/src/main/java/com/example/solarexplorer/viewmodel/` — ViewModels and factories
- `app/src/main/res/drawable/` — planet images
- `app/src/main/res/raw/` — Lottie JSON animations (`solar_system.json`, `planet_orbit.json`)

---

## How to run (local)
1. Clone the repo:
```bash
git clone <repo-url>
cd <repo-name>
