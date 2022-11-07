package com.example.corona19vaccinemapservice.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class VaccineStations(
    var stationMap: HashMap<Int, List<Data>> = HashMap<Int,List<Data>>()
) : Parcelable
