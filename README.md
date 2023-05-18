# Weather

## Exercise
It is necessary to implement an application that displays the weather forecast.

### Application requirements:
Displaying the weather forecast for the current day
Weekly forecast display
Possibility to see the weather forecast in the current city
The ability to choose any other city and find out the weather forecast in it

## Description
The application uses [One Call API 3.0](https://openweathermap.org/api/one-call-3) to obtain weather data и [Geocoding API](https://openweathermap.org/api/geocoding-api) to find a city that can be added to track the weather. Data with added cities is stored in the database.

For API use Retrofit, for database Room.

To build the project and use the API, you need to get the API Key from [website](https://openweathermap.org/api). Then create a file ***apikey.properties*** in the root of the project.
In it, save the line:
>weather_one_call_api_key="received API Key"

<img src="https://user-images.githubusercontent.com/43324144/201521592-67025b45-ea42-4310-9e3a-6bc847ab15fa.jpg" width="270" height="600">
