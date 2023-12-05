# WeatherPlan 🌥️

## Introduction 
WeatherPlan is an innovative Android application developed using Kotlin and Android Studio, designed to provide real-time weather updates and forecasts. This app stands out with its user-friendly interface, detailed weather data display, and unique features like calendar integration and graphical weather forecasts. Whether you're planning your day or checking weather conditions in different cities, WeatherPlan is your go-to source for accurate and up-to-date weather information.

## Permission
Our app will request access to the user's device location and calendar, ensuring they are informed and their consent is obtained. This process involves clearly explaining why these permissions are needed and how they enhance the app's functionality. The app will prompt the user for these permissions, typically through a standard system dialog, ensuring transparency and trust. Users will have the ability to grant or deny these permissions based on their comfort and understanding of the app's requirements. Additionally, the app will provide easy options for users to manage their preferences and change their permissions at any time in the app settings, ensuring ongoing user control and privacy.

## Use cases
The use cases for a weather app as described can be summarized as follows:

1. **Automatic Location Detection and Current Weather Display:**
> When the user opens the app, the app ask for permission to access user's location. Then user have the options to accept and deny the permission.
After user grants permission, it automatically detects their current location.
The app displays the current weather conditions for the user's location.
The main page also shows a 5-day weather forecast.
Users can scroll horizontally to view detailed weather information for the upcoming days.

2. **Manual Location Search for Weather Updates:**
> Users have the option to search for a specific city.
Upon entering a city name, the app updates to show the weather for that location.
The 5-day forecast displayed is also based on the newly selected location.

4. **Calendar Integration with Weather Forecasts:**
> Initially, the user's calendar is not automatically updated with the latest weather forecast.
The user can opt to update their calendar by clicking "Set Calendar".
After clicking the Set Calendar the user will see another option to give or deny permission to access the calendar.
After granting permission to the calendar, the calendar is updated, the calendar will include a 5-day weather forecast.
This feature allows users to see weather predictions when setting reminders or planning events.

6. **Graphical Weather Information View:**
> The app includes a graph view that can be accessed through a slide gesture (sliding left from the main page).
The graph view presents detailed information on temperature, wind speed, humidity, and "feels like" temperature.
This view is scrollable, allowing users to explore weather trends over time.
Users can interact with checkboxes to select which weather information (temperature, wind speed, humidity, feels like) to display on the graph.
Unchecking a box removes that particular information from the graph.
A **"back"** button allows users to return to the main page from the graph view.


## Main Feature
A detailed description of each feature is given below: -
1. **Current Location Detection:** The app automatically detects the user’s current geographical location. This feature is vital for providing localized weather information as soon as the app is launched, enhancing user convenience.

2. **Real-time Weather Data Updates:** Utilizing the OpenWeather API, the app delivers up-to-date weather information. This feature ensures that users receive the most current weather data, including unexpected changes in weather conditions.

3. **Display of Comprehensive Weather Data:** The app displays an array of weather-related data including maximum and minimum temperatures, sunset and sunrise times, humidity levels, wind speed, general weather conditions (e.g., sunny, cloudy), and sea level pressure. This comprehensive data set provides users with a thorough understanding of the current weather.

4. **City Search Functionality:** Users can search for and receive weather updates for different cities worldwide. This feature is particularly useful for planning travel or checking on the weather in other locations.

5. **User-Friendly UI with Dynamic Changes:** The app boasts a user-friendly interface where background images and icons dynamically change according to the current weather conditions. This visual representation enhances user experience by making the weather data more relatable and easier to understand.

6. **5-Day Weather Forecast:** A unique feature of the app is its 5-day weather forecast, presented with a horizontal scroll view. This view provides users with a quick glance at the upcoming weather, detailing maximum and minimum temperatures and humidity levels for each day.

7. **Calendar Integration:** The app integrates with the device’s built-in calendar, allowing users to view the 5-day weather forecast within their calendar. This feature is particularly useful for planning events or reminders based on expected weather conditions.

8. **Graphical Representation of Weather Forecast:** Another innovative feature is the graphical representation of the 5-day weather forecast. Accessible through a swipe gesture, this graph offers users a visual representation of temperature trends, humidity, and wind speeds, making the interpretation of weather data more intuitive.


## Prerequisites
Before installing WeatherPlan, ensure you have:
- An Android device capable of running the app.
- Android Studio for development and testing purposes.
- It is not recommended to use an emulator for testing due to certain feature limitations.
- Make sure to enable Animation in the device.

## Version History
- **Oct 5**: Implemented Linear Layout for structured UI.
- **Oct 21**: Completed Front-End Layout with intuitive design.
- **Nov 7**: Integrated OpenWeather API for live weather data.
- **Nov 10**: Integrated Calendar feature for planning and reminders.
- **Nov 16**: Implemented 5-day weather forecast with calendar integration.
- **Dec 4**: Added Graphical Representation of the 5-day weather forecast.
- **Dec 4**: Implemented Swipe and Scroll gestures for enhanced user interaction.
- **Dec 5**: Submitted the final version of the project.

## Authors
- **Md Jahid** - Project Manager, Assistanat Developer
  1. Setting up git.
  2. Creating the Main layout
  3. Rest of the work is shared
- **Md Jubaer** - Main Developer
  1. Integration of Animation
  2. Integrtation of Graph
  3. Integration of Calendar
  4. Rest of the work is shared

  ***Note: All the work has been pushed from one laptop. Although both the authors worked together.***

## References
- OpenWeather API **version 2.5**.
- Resources for Calendar [here](https://developer.android.com/reference/kotlin/android/icu/util/Calendar)
- Resources for Animation [here](https://www.geeksforgeeks.org/android-animations-in-kotlin/)
- Resources for Line Graph [here](https://www.geeksforgeeks.org/android-line-graph-view-with-kotlin/)
- Resources for Background Image [here](https://medium.com/@josephajire/how-to-add-background-image-to-your-android-project-with-jetpack-compose-1c5392967fd5)
- Other Resources [here](https://medium.com/@mutebibrian256/creating-weather-app-using-kotlin-an-android-application-1c5d39f376c5)
- Other Resources [here](https://www.appsloveworld.com/kotlin/100/109/change-background-image-using-data-binding-kotlin)
