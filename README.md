Sunshine

### Open Weather Map API Key is required.

In order for the Sunshine app to function properly as of October 18th, 2015 an API key for openweathermap.org must be included with the build.

Obtain a key via the following [instructions](http://openweathermap.org/appid#use), and include the unique key for the build by adding the following line to [USER_HOME]/.gradle/gradle.properties

`MyOpenWeatherMapApiKey="<UNIQUE_API_KEY">`


### Also needed is a 'google-services.json' config file for Google Cloud Messaging

Obtain this file following these [instructions] (https://developers.google.com/cloud-messaging/android/client#get-config).

