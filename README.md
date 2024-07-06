# Catapult

Catapult is a quiz application about cat breeds. It allows users to create a local account, browse through a catalog of cat breeds and images, participate in a knowledge quiz about cats, 
and view a leaderboard of quiz results.

## Functional Specifications

- Local account creation
- Catalog of cat breeds (including photos)
- Cat knowledge quiz
- Leaderboard
- Account details and editing

## Installation

To install and run this project, follow these steps:

1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Sync the project to download all necessary dependencies.
4. Run the project on an emulator or a physical device.

## Using Your Own Cats API Key

The application uses the Cats API to fetch data about cat breeds. If you want to use your own API key, follow these steps:

1. Obtain an API key from [TheCatAPI](https://thecatapi.com/).
2. Open the [`NetworkingModule.kt`](https://github.com/mradovic38/catapult/blob/master/app/src/main/java/rs/raf/catapult/networking/di/NetworkingModule.kt) file.
4. Replace the existing API key in the `addInterceptor` method with your own key.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
