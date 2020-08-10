package com.example.clima.entity

import com.google.gson.annotations.SerializedName

data class UnaHora(@SerializedName("Temperature")
                   val temperature: Temperature,
                   @SerializedName("PrecipitationProbability")
                   val precipitationProbability: Int = 0,
                   @SerializedName("HasPrecipitation")
                   val hasPrecipitation: Boolean = false,
                   @SerializedName("IconPhrase")
                   val iconPhrase: String = "",
                   @SerializedName("IsDaylight")
                   val isDaylight: Boolean = false,
                   @SerializedName("DateTime")
                   val dateTime: String = "",
                   @SerializedName("WeatherIcon")
                   val weatherIcon: Int = 0)