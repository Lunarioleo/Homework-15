package com.example.homework15

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {


    @GET("{cityName}")
    fun findCityByName(@Path("cityName") cityName: String):Single <WeatherForecast>

    @GET("Lviv")
    fun findCity(): Single<WeatherForecast>
}