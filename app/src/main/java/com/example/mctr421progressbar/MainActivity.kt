@file:Suppress("UNREACHABLE_CODE")

package com.example.mctr421progressbar

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.mctr421progressbar.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {
    //creation of variables that would be used
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private var light:Int = 100
    private var voltage:Int = 100
    private var water:Int = 100
    private var moisture:Int = 100

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toggleButton2.setOnClickListener{ setDataON() } //code to make toggle button turn on spotlight
        //it uses that setDataON function to update value on the database

        binding.toggleButton.setOnClickListener{ setPumpON() }

        //decision statement to determine if the updated value should be 255 or 0. 255 means on and 0 means off
        val toggleButton2 = findViewById<ToggleButton>(R.id.toggleButton2)
        toggleButton2.setOnClickListener{
            if(toggleButton2.text.toString() == "ON"){
                setDataON()
            }
            else{
                setDataOFF() //set dataOff function updates value on database to 0
            }
        }

        val toggleButton = findViewById<ToggleButton>(R.id.toggleButton)
        toggleButton.setOnClickListener{
            if(toggleButton.text.toString() == "ON"){
                setPumpON()
            }
            else{
                setPumpOFF() //set dataOff function updates value on database to 0
            }
        }

        //button will be used to update the values of the progress bars
        val updateButton = findViewById<Button>(R.id.button)
        updateButton.setOnClickListener {
            readData() //function made to read the values of the database

//initializing the textviews to show values
            val txtWater = findViewById<TextView>(R.id.textView5)
            val txtMoisture = findViewById<TextView>(R.id.textView6)
            val txtlight = findViewById<TextView>(R.id.textView7)
            val txtBattery = findViewById<TextView>(R.id.textView8)

            //the textvews will be updated with the percentage values from the database
            txtWater.text = "$water%"
            txtMoisture.text = "$moisture%"
            txtlight.text = "$light%"
            txtBattery.text = "$voltage%"

            //ProgressBar will be responsible for the water level of the reservoir
            val progressBar = findViewById<ProgressBar>(R.id.progressBar)
            progressBar.max = 100

            //ProgressBar2 will be in charge of the moisture level of the soil
            val progressBar2 = findViewById<ProgressBar>(R.id.progressBar2)
            progressBar2.max = 100

            //ProgressBar3 will be in charge of the light level the plant is getting
            val progressBar3 = findViewById<ProgressBar>(R.id.progressBar3)
            progressBar3.max = 100

            //ProgressBar4 will be in charge of the moisture level of the soil
            val progressBar4 = findViewById<ProgressBar>(R.id.progressBar4)
            progressBar4.max = 100

            //Code to animate the changes that occur on the progress bars
            ObjectAnimator.ofInt(progressBar3, "progress", light)
                .setDuration(1000)
                .start()

            ObjectAnimator.ofInt(progressBar4, "progress", voltage)
                .setDuration(1000)
                .start()

            ObjectAnimator.ofInt(progressBar, "progress", water)
                .setDuration(1000)
                .start()

            ObjectAnimator.ofInt(progressBar2, "progress", moisture)
                .setDuration(1000)
                .start()


            if(light>50){
                binding.plantFall.playAnimation()
            }


        }

    }

    private fun setDataOFF(){
        database = FirebaseDatabase.getInstance().getReference("LED")
        database.child("spotlight").setValue(0)
    }
    private fun setDataON() {
        database = FirebaseDatabase.getInstance().getReference("LED")
        database.child("spotlight").setValue(255)
    }

    private fun setPumpON(){
        database = FirebaseDatabase.getInstance().getReference("LED")
        database.child("pumpState").setValue(255)
    }

    private fun setPumpOFF(){
        database = FirebaseDatabase.getInstance().getReference("LED")
        database.child("pumpState").setValue(0)
    }

    @SuppressLint("SetTextI18n")
    private fun readData(): Int {
        database = FirebaseDatabase.getInstance().getReference("Sensor")
        database.child("Moisture_Percent").get().addOnSuccessListener {
            if(it.exists()){
                moisture = it.value.toString().toInt()
               // binding.textView6.text = "$moisture%"
            }
        }
        database = FirebaseDatabase.getInstance().getReference("Sensor")
        database.child("water_Percent").get().addOnSuccessListener {
            if(it.exists()){
                water = it.value.toString().toInt()
                //binding.textView5.text = "$water%"
            }
        }
        database = FirebaseDatabase.getInstance().getReference("Sensor")
        database.child("voltage_Percent").get().addOnSuccessListener {
            if(it.exists()){
                voltage = it.value.toString().toInt()
               // binding.textView8.text = "$voltage%"
            }
        }

        database = FirebaseDatabase.getInstance().getReference("Sensor")
        database.child("light_Percent").get().addOnSuccessListener {
            if(it.exists()){
                light = it.value.toString().toInt()
                //binding.textView7.text = "$light%"
            }
        }
        return light
        return voltage
        return water
        return moisture
    }

}

