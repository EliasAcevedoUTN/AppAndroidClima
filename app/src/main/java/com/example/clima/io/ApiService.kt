package com.example.clima.io

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    @GET("/locations/v1/cities/geoposition/search?")
    fun getLocation(@Query("apikey" ) apiKey : String,
                    @Query("q") q : String,
                    @Query("language") language : String): Call<JsonElement>

    @GET("/forecasts/v1/daily/{day}/{key}?")
    fun getDay(@Path("day") day: String,
               @Path("key" ) key : Int,
               @Query("apikey") apiKey : String,
               @Query("language") language : String,
               @Query("metric") metric : Boolean): Call<JsonElement>


    @GET("/forecasts/v1/hourly/1hour/{key}?")
    fun getHour(@Path("key" ) key : Int,
                @Query("apikey") apiKey : String,
                @Query("language") language : String,
                @Query("metric") metric : Boolean): Call<JsonElement>

}