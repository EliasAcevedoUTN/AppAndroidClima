package com.example.clima.entity

import com.google.gson.annotations.SerializedName

data class Minimum(@SerializedName("UnitType")
                   val unitType: Int = 0,
                   @SerializedName("Value")
                   val value: Double = 0.0,
                   @SerializedName("Unit")
                   val unit: String = "")