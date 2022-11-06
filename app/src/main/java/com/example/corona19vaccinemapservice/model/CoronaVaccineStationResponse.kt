package com.example.corona19vaccinemapservice.model


import com.google.gson.annotations.SerializedName

data class CoronaVaccineStationResponse(
    @SerializedName("currentCount")
    val currentCount: Int?,
    @SerializedName("data")
    val `data`: List<Data>?,
    @SerializedName("matchCount")
    val matchCount: Int?,
    @SerializedName("page")
    val page: Int?,
    @SerializedName("perPage")
    val perPage: Int?,
    @SerializedName("totalCount")
    val totalCount: Int?
)