package ca.unb.mobiledev.weatherapp

data class ForecastData(
    val clouds: CloudsX,
    val dt: Int,
    val dt_txt: String,
    val main: MainX,
    val pop: Double,
    val rain: Rain,
    val snow: Snow,
    val sys: SysX,
    val visibility: Int,
    val weather: List<WeatherX>,
    val wind: WindX
)


//package ca.unb.mobiledev.weatherapp
//
//data class (
//    val clouds: CloudsX,
//    val dt: Int,
//    val dt_txt: String,
//    val main: MainX,
//    val pop: Double,
//    val rain: Rain,
//    val snow: Snow,
//    val sys: SysX,
//    val visibility: Int,
//    val weather: List<WeatherX>,
//    val wind: WindX
//)