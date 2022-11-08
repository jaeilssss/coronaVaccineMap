package com.example.corona19vaccinemapservice.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoadingViewModel : ViewModel() {


    var progress : MutableLiveData<Int> = MutableLiveData(0)
    var progressStr : MutableLiveData<String> = MutableLiveData("")
}