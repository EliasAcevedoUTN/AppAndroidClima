package com.example.clima.entity


import com.google.gson.annotations.SerializedName

data class Temperature(@SerializedName("Minimum")
                       val minimum: Minimum,
                       @SerializedName("Maximum")
                       val maximum: Maximum,
                       @SerializedName("UnitType")
                       val unitType: Int = 0,
                       @SerializedName("Value")
                       val value: Double = 0.0,
                       @SerializedName("Unit")
                       val unit: String = "")