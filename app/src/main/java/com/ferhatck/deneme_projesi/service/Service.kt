package com.ferhatck.deneme_projesi.service

import com.ferhatck.deneme_projesi.modal.ForeCast
import com.ferhatck.deneme_projesi.Utils
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("forecast?")
    fun getCurrentWeather(
        @Query("lat")
        lat: String,
        @Query("lon")
        lon: String,
        @Query("appid")
        appid: String = Utils.API_KEY

    ): Call<ForeCast>


    @GET("forecast?")
    fun getWeatherByCity(
        @Query("q")
        city: String,
        @Query("appid")
        appid: String = Utils.API_KEY

    ): Call<ForeCast>




}