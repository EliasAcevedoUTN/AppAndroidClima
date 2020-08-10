package com.example.clima.entity
import com.google.gson.annotations.SerializedName
data class PronosticoDiario(@SerializedName("Temperature")
                       val temperature: Temperature,
                       @SerializedName("Night")
                       val night: Night,
                       @SerializedName("EpochDate")
                       val epochDate: Int = 0,
                       @SerializedName("Day")
                       val day: Day,
                       @SerializedName("Sources")
                       val sources: List<String>?,
                       @SerializedName("Date")
                       val date: String = "",
                       @SerializedName("Link")
                       val link: String = "",
                       @SerializedName("MobileLink")
                       val mobileLink: String = "")