package com.example.corona19vaccinemapservice.service

import com.example.corona19vaccinemapservice.model.CoronaVaccineStationResponse
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Query

interface CoronaVaccineSearchService {


    @GET("centers")
    suspend fun getVaccineStationInfo(
        @Query("page") page : Int,
        @Query("perPage") perPage : Int,
        @Query("serviceKey") serviceKey : String
    ): Response<CoronaVaccineStationResponse>
}