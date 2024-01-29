package com.example.homework15


import com.example.homework15.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnCheck.setOnClickListener {
            binding.topText.text = ("Weather in \n" + binding.edText.text.toString())
            val weather = ApiClient.weather.create(ApiInterface::class.java)
            weather.findCityByName(binding.edText.text.toString())

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({weatherForecast->
                    parseData(weatherForecast, R.id.temperature, R.id.wind, "Current")
                    parseData(weatherForecast, R.id.temperatureDay1, R.id.windDay1,  "1")
                    parseData(weatherForecast, R.id.temperatureDay2, R.id.windDay2,  "2")
                    parseData(weatherForecast, R.id.temperatureDay3, R.id.windDay3,  "3")
                    binding.mainCard.visibility = View.VISIBLE
                    binding.showMore.visibility = View.VISIBLE
                },{
                    Toast.makeText(this, "Oops! Cant reach data, try again later!", Toast.LENGTH_SHORT).show() // Сайт час від часу падає, і апка не працює
                })

        }
        binding.showMore.setOnClickListener {
            binding.threeDayWForecast.visibility = View.VISIBLE
        }

    }
    fun checkImage(w: WeatherForecast, id: Int){
        val image = findViewById<ImageView>(id)
        when {
             (w.description.contains("snow"))->{
                image.setImageResource(R.drawable.snowy)
                }
            (w.description.contains("rain"))->{
                image.setImageResource(R.drawable.rainy)
            }
            (w.description.contains("sun"))->{
                image.setImageResource(R.drawable.day)
            }
            (w.description.contains("cloud"))->{
                image.setImageResource(R.drawable.cloudy_day)
            }
        }
    }



    fun parseData(w: WeatherForecast, temperatureId: Int, windId: Int, dayNumber: String){
        binding.description.text = w.description
        val temperature = findViewById<TextView>(temperatureId)
        val wind = findViewById<TextView>(windId)
        when {
            (dayNumber == "Current")->{
                temperature.text = w.temperature
                wind.text = w.wind
                checkImage(w, R.id.weatherImage)
            }
            (dayNumber == "1")->{
                temperature.text = w.forecast[0].temperature
                wind.text = w.forecast[0].wind
            }
            (dayNumber == "2")->{
                temperature.text = w.forecast[1].temperature
                wind.text = w.forecast[1].wind
            }
            (dayNumber == "3")->{
                temperature.text = w.forecast[2].temperature
                wind.text = w.forecast[2].wind
            }
        }


    }
}





data class WeatherForecast (val temperature: String, val wind: String, val description: String, val forecast: ArrayList<DailyForecast> )

data class DailyForecast(val day: String, val temperature: String, val wind: String)