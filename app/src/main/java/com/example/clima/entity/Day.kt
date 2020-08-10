package com.example.clima.entity

import com.google.gson.annotations.SerializedName

class Day (@SerializedName("HasPrecipitation")
           val hasPrecipitation: Boolean = false,
           @SerializedName("IconPhrase")
           val iconPhrase: String = "",
           @SerializedName("Icon")
           val icon: Int = 0)