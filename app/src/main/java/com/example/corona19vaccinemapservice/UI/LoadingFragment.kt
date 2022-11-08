package com.example.corona19vaccinemapservice.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.corona19vaccinemapservice.R
import com.example.corona19vaccinemapservice.Repository
import com.example.corona19vaccinemapservice.databinding.LoadingFragmentBinding
import com.example.corona19vaccinemapservice.model.Data
import com.example.corona19vaccinemapservice.model.VaccineStations
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoadingFragment : Fragment() {

    companion object {
        fun newInstance() = LoadingFragment()
    }

    private lateinit var viewModel: LoadingViewModel

    private lateinit var binding : LoadingFragmentBinding

    private lateinit var navController: NavController
    private val scope = MainScope()
    var map = HashMap<Int,List<Data>>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.loading_fragment,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoadingViewModel::class.java)

        navController = findNavController()

        getVaccineStationInfo()

        increaseProgressBar()
    }

    private fun getVaccineStationInfo(){
        scope.launch {

            for(i in 1..10){
                var response = Repository.getVaccineStation(i)

                if(response?.data!=null){
                    map.put(i, response?.data!!)
                }
            }
        }
    }

    private fun increaseProgressBar(){
        var count = 0
        Thread(Runnable {
            while (true){
                if(count==80 && map.size!=10){
                    continue
                }

                    scope.launch {
                        viewModel.progress.value =count
                        viewModel.progressStr.value = "$count%"
                        binding.model = viewModel
                        count++
                    }



                if(count>100) {
                    goToNextView()
                    break
                }
                Thread.sleep(400) //0.4초 간격으로 1%씩 증가
            }
        }).start()


    }

    private fun goToNextView(){
        scope.launch {
            val navAction = LoadingFragmentDirections.actionLoadingFragmentToMapFragment(VaccineStations(map))
            navController.navigate(navAction)

        }
    }


}