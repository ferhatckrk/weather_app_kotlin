package com.ferhatck.deneme_projesi.mvvm
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferhatck.deneme_projesi.MyApplication
import com.ferhatck.deneme_projesi.SharedPrefs
import com.ferhatck.deneme_projesi.Utils
import com.ferhatck.deneme_projesi.modal.WeatherList
import com.ferhatck.deneme_projesi.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherVm : ViewModel() {

    val todayWeatherLiveData = MutableLiveData<List<WeatherList>>()
    val forecastWeatherLiveData = MutableLiveData<List<WeatherList>>()
    val context = MyApplication.instance

    val closetorexactlysameweatherdata = MutableLiveData<WeatherList?>()
    val cityName =MutableLiveData<String>()


    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeather(city: String? = null) = viewModelScope.launch(Dispatchers.IO)  {

        val todayWeatherList = mutableListOf<WeatherList>()

        val currentDateTime =LocalDateTime.now()
        val currentDatePattern = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val sharedPrefs =SharedPrefs(context)
        val lat = sharedPrefs.getValue("lat").toString()
        val lon = sharedPrefs.getValue("lon").toString()

        val call = if (city != null) {
            RetrofitInstance.api.getWeatherByCity(city)
        }
        else {
            RetrofitInstance.api.getCurrentWeather( lat , lon )
        }
        val response =call.execute()


        if(response.isSuccessful){
            val weatherList = response.body()?.weatherList
            cityName.postValue(response.body()?.city!!.name)


            val presenDate = currentDatePattern

            weatherList!!.forEach{ weather ->

                if(weather.dtTxt!!. split("\\s".toRegex()).contains(presenDate)) {
                    todayWeatherList.add(weather)
                }
            }

            val closetWeather = findClosedWeather(todayWeatherList)

            closetorexactlysameweatherdata.postValue(closetWeather)


            todayWeatherLiveData.postValue(todayWeatherList)



        }





 }





}