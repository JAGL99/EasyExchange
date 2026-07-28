# Narwal-ExchangeApp

A currency exchange application for Android.

## Tech Stack

- Android
- Kotlin
- Jetpack Compose
- Hilt
- Retrofit
- Room

## Build

To build the project, run:

```shell
./gradlew build
```


## Add google-services as secret

To add the google-services.json file as secret for actions, you need to generate a 64 base string of the file, run this comand on your file location:

````shell
base64 -i google-services.json -b 0
````
At last, paste the result string where you need to use the google-services.json as base 64 string
