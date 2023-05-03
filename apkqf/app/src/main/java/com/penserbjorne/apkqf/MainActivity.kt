package com.penserbjorne.apkqf

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.penserbjorne.apkqf.databinding.ActivityMainBinding

private const val TAG = "apkqf"

class MainActivity : AppCompatActivity() {
    /*
    // Changed for a viewBinding feature
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonRunExtraction: Button = findViewById(R.id.buttonRunExtraction)
        buttonRunExtraction.setOnClickListener {
            runExtractions()
        }
    }
    */

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Assign execution to button
        binding.buttonRunExtraction.setOnClickListener { runExtractions() }
    }

    private fun runExtractions() {
        // ToDo: Validar si esta seleccionado el checkbox, validar permisos, validar comandos
        val myAcquisition = Acquisition(applicationContext)
        myAcquisition.test()
        myAcquisition.initialize()
        myAcquisition.getBackup()
        myAcquisition.getProp()
        myAcquisition.getSettings()
        myAcquisition.getProcesses()
        myAcquisition.getServices()
        myAcquisition.getLogcat()
        myAcquisition.getLogs()
        myAcquisition.getDumpSys()
        myAcquisition.getPackages()
    }

}