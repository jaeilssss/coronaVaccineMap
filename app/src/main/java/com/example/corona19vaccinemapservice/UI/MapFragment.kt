package com.example.corona19vaccinemapservice.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.corona19vaccinemapservice.R
import com.example.corona19vaccinemapservice.databinding.MapFragmentBinding
import com.example.corona19vaccinemapservice.model.Data
import com.example.corona19vaccinemapservice.model.VaccineStations
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage

class MapFragment : Fragment(), OnMapReadyCallback, Overlay.OnClickListener {

    companion object {
        fun newInstance() = MapFragment()
    }

    private lateinit var viewModel: MapViewModel
    private lateinit var binding : MapFragmentBinding

    private lateinit var navController: NavController

    private lateinit var naverMap: NaverMap

    private lateinit var list : ArrayList<Data>

    private  var currentClickIndex=-1

    var count = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.map_fragment,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        navController = findNavController()

        val args : MapFragmentArgs by navArgs()

        val item = args.item
        setMarker(item)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { naverMap = it }



        binding.VaccineStationExit.setOnClickListener {
            currentClickIndex = -1

            binding.vaccineStationLayout.visibility = View.GONE
        }

    }


    fun setMarker(item : VaccineStations){
        var stationMap = item.stationMap
        list = ArrayList()
        var index=0
        for(key in stationMap.keys){
            var data = stationMap[key]



            for(i in data!!.indices){
                var marker = Marker()
                list.add(data[i])
                marker.position = LatLng(data[i].lat!!.toDouble(),data[i].lng!!.toDouble())
                marker.zIndex = index++

                if(data[i].centerType=="중앙/권역"){
                    marker.icon = OverlayImage.fromResource(R.drawable.ic_location_on_yellow_24)
                }else if(data[i].centerType=="지역"){
                    marker.icon = OverlayImage.fromResource(R.drawable.ic_location_on_green_24)
                }
                marker.setOnClickListener(this)
                marker.width = 140
                marker.height = 140
                binding.mapView.getMapAsync {
                    marker.map = it
                }

            }


        }
    }



    override fun onMapReady(p0: NaverMap) {
    }

    override fun onClick(overlay: Overlay): Boolean {
      if(overlay is Marker){

          var cindex = overlay.zIndex
          if(currentClickIndex!=cindex){
              var data = list[cindex]
              val camera = CameraUpdate.scrollTo(LatLng(data.lat!!.toDouble(),data.lng!!.toDouble())).animate(CameraAnimation.Easing)
              overlay.map!!.moveCamera(camera)

              binding.Phonenumber.setText(data.phoneNumber)
              binding.vaccineStationAddress.setText(data.address)
              binding.centerName.setText(data.centerName)
              binding.vaccineStationName.setText(data.facilityName)
              binding.updateAt.setText(data.updatedAt)
              binding.vaccineStationLayout.visibility = View.VISIBLE
              currentClickIndex = cindex

              if(data.centerType=="중앙/권역"){
                  binding.vaccineStationImg.setImageResource(R.drawable.ic_outline_location_city_24)
              }else{
                  binding.vaccineStationImg.setImageResource(R.drawable.ic_outline_location_city_green_24)

              }
          }else{
              currentClickIndex = -1

              binding.vaccineStationLayout.visibility = View.GONE
          }


          return true
      }
        return true
    }
}