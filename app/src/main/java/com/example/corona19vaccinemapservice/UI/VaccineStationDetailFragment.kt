package com.example.corona19vaccinemapservice.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.corona19vaccinemapservice.R
import com.example.corona19vaccinemapservice.databinding.VaccineStationFragmentBinding
import com.example.corona19vaccinemapservice.model.Data

class VaccineStationDetailFragment(data: Data) : DialogFragment() {

    private lateinit var data : Data
    init {
        this.data = data
    }


    private lateinit var detailViewModel: VaccineStationDetailViewModel

    private lateinit var binding : VaccineStationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.vaccine_station_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        detailViewModel = ViewModelProvider(this).get(VaccineStationDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}