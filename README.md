# PSCodeExercise
## Overview
The PSCodeExercise app fetches data from a json file resource and displays a list of drivers. When a driver is selected from the list, a shipment is offered to the driver in a way that maximizes the total Suitability Score over the set of drivers according to the following business rules:

1. If the length of the shipment's destination street name is even, the base suitability score (SS) is the number of vowels in the driver's name multiplied by 1.5.
2. If the length of the shipment's destination street name is odd, the base SS is the number of consonants in the driver's name multipled by 1.
3. If the length of the shipment's destination street name shares any common factors (besides 1) with the length of the driver's name, the SS is increased by 50% above the base SS.

## Tech Stack

- Kotlin
- Jetpack Compose
- Hilt (Dependency Injection)
- Gson
- State Flow
- Coroutines

## Run Instructions

To run the PSCodeExercise app please follow the following steps:

1. Pull the repo from GitHub
2. Open project in Android Studio
3. Build project
4. Use the 'Run' button in Android Studio to install app on emulator or physical device

** This project requires your device be on Android SDK 26 or higher.

## Package Structure

![Package Structure Image](../../Downloads/PSCodeExercise.png)