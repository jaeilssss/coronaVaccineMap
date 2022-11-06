package com.example.corona19vaccinemapservice

import com.example.corona19vaccinemapservice.Service.CoronaVaccineSearchService
import com.example.corona19vaccinemapservice.model.CoronaVaccineStationResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object Repository {


    suspend fun getVaccineStation(page: Int): CoronaVaccineStationResponse? {
        println("0--------00")
        println(coronaVaccineSearchService
            .getVaccineStationInfo(page,10,BuildConfig.CORONA_VACCINE_SEARCH_SERVICE_KEY)
            .body())
        return  coronaVaccineSearchService
            .getVaccineStationInfo(page,10,BuildConfig.CORONA_VACCINE_SEARCH_SERVICE_KEY)
            .body()

    }



    private val coronaVaccineSearchService: CoronaVaccineSearchService by lazy {
        Retrofit.Builder()
            .baseUrl(Url.VACCINE_STATION_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClient())
            .build()
            .create()


    }

    private fun buildHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
            ).build()
}