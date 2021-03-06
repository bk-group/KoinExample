package com.github.harmittaa.koinexample.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.harmittaa.koinexample.R
import com.github.harmittaa.koinexample.databinding.FragmentViewBinding
import com.github.harmittaa.koinexample.model.TempData
import com.github.harmittaa.koinexample.model.Weather
import com.github.harmittaa.koinexample.networking.Resource
import com.github.harmittaa.koinexample.networking.Status
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val fragmentModule = module {
    factory { WeatherFragment() }
}

class WeatherFragment : Fragment() {
    private val exampleViewModel: WeatherViewModel by viewModel()
    private lateinit var binding: FragmentViewBinding

    private val observer = Observer<Resource<Weather>> {
        when (it.status) {
            Status.SUCCESS -> updateTemperatureText(it.data!!.name, it.data.temp)
            Status.ERROR -> showError(it.message!!)
            Status.LOADING -> showLoading()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view, container, false)
        binding.viewModel = exampleViewModel
        exampleViewModel.weather.observe(this, observer)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun showLoading() {
        binding.weatherInfo.text = "Loading..."
    }

    @SuppressLint("SetTextI18n")
    private fun showError(message: String) {
        binding.weatherInfo.text = "Error: $message"
    }

    @SuppressLint("SetTextI18n")
    private fun updateTemperatureText(name: String, temp: TempData) {
        binding.weatherInfo.text = "Temperature at ${name} is ${temp.temp} celsius"
    }
}