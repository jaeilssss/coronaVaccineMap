package com.example.corona19vaccinemapservice.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.corona19vaccinemapservice.R
import com.example.corona19vaccinemapservice.databinding.MapFragmentBinding
import com.example.corona19vaccinemapservice.model.Data
import com.example.corona19vaccinemapservice.model.VaccineStations
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
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

    private val REQUEST_LOCATION_CODE = 1000

    private var cancellationTokenSource : CancellationTokenSource ? =null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    private val scope = MainScope()

    private var check = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.map_fgit ragment,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        binding.model = viewModel
        navController = findNavController()
        getCurrentLocation()
        val args : MapFragmentArgs by navArgs()

        val item = args.item
        setMarker(item)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { naverMap = it }



        binding.VaccineStationExit.setOnClickListener {
            currentClickIndex = -1

            binding.vaccineStationLayout.visibility = View.GONE
        }

        binding.goToCurrentLocation.setOnClickListener{

            getCurrentLocation()
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
                marker.captionText = data[i].facilityName!!
                binding.mapView.getMapAsync {
                    marker.map = it
                }

            }


        }
    }

    private fun permission(){
        requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION),REQUEST_LOCATION_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if(grantResults.size>=2){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED &&
                grantResults[1]==PackageManager.PERMISSION_GRANTED){

                getCurrentLocation()
            }else{

            }
        }

    }

    private fun getCurrentLocation(){
        cancellationTokenSource = CancellationTokenSource()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource!!.token
            ).addOnSuccessListener {

                scope.launch {
                    if(!check){ //최초 내 위치 좌표 획득했을 경우
                        var marker = Marker()

                        marker.width = 140
                        marker.height = 140
                        marker.icon = OverlayImage.fromResource(R.drawable.ic_location_on_black_24)
                        marker.captionText = "내 위치"
                        marker.position = LatLng(it.latitude,it.longitude)
                        binding.mapView.getMapAsync {
                            marker.map = it
                        }
                        check  = true
                    }



                    moveToMapCamera(it.latitude,it.longitude)
                }


            }.addOnFailureListener {
                println("실퓨ㅐ")
            }
        }else{
//            Toast.makeText(context,"")

        permission()
        }


    }

    fun moveToMapCamera(lat : Double , lng : Double){


        val camera = CameraUpdate.scrollTo(LatLng(lat,lng)).animate(CameraAnimation.Easing)
        binding.mapView.getMapAsync {
            it.moveCamera(camera)
        }
    }
    override fun onMapReady(p0: NaverMap) {
    }

    override fun onClick(overlay: Overlay): Boolean {
      if(overlay is Marker){

          var cindex = overlay.zIndex
          if(currentClickIndex!=cindex){
              var data = list[cindex]


              moveToMapCamera(data.lat!!.toDouble(),data.lng!!.toDouble())


              viewModel.phoneNumber.value = data.phoneNumber
              viewModel.address.value = data.address

              viewModel.centerName.value = data.centerName
              viewModel.facilityName.value = data.facilityName


              viewModel.updatedAt.value = data.updatedAt

              viewModel.layout.value = View.VISIBLE
              binding.model = viewModel

              currentClickIndex = cindex

              if(data.centerType=="중앙/권역"){
                  binding.vaccineStationImg.setImageResource(R.drawable.ic_outline_location_city_24)
              }else{
                  binding.vaccineStationImg.setImageResource(R.drawable.ic_outline_location_city_green_24)

              }
          }else{
              currentClickIndex = -1

              viewModel.layout.value = View.GONE
              binding.model = viewModel
          }


          return true
      }
        return true
    }
}