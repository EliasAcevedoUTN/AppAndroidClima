package com.example.clima.entity

import com.google.gson.annotations.SerializedName

data class Headline(@SerializedName("Category")
                    val category: String = "",
                    @SerializedName("EndEpochDate")
                    val endEpochDate: Int = 0,
                    @SerializedName("EffectiveEpochDate")
                    val effectiveEpochDate: Int = 0,
                    @SerializedName("Severity")
                    val severity: Int = 0,
                    @SerializedName("Text")
                    val text: String = "",
                    @SerializedName("EndDate")
                    val endDate: String = "",
                    @SerializedName("Link")
                    val link: String = "",
                    @SerializedName("EffectiveDate")
                    val effectiveDate: String = "",
                    @SerializedName("MobileLink")
                    val mobileLink: String = "")