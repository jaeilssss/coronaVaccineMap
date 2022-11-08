package com.example.corona19vaccinemapservice.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.view.View
class MapViewModel : ViewModel() {
    var address  : MutableLiveData<String> = MutableLiveData("")
    var centerName : MutableLiveData<String> = MutableLiveData("")
    var facilityName : MutableLiveData<String> = MutableLiveData("")
    var phoneNumber : MutableLiveData<String> = MutableLiveData("")
    var updatedAt : MutableLiveData<String> = MutableLiveData("")

    var layout : MutableLiveData<Int> = MutableLiveData(View.GONE)
    
}