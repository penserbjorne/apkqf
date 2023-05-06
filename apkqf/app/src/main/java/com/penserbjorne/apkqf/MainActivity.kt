package com.penserbjorne.apkqf

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.penserbjorne.apkqf.databinding.ActivityMainBinding

private const val TAG = "apkqf"
private const val REQUEST_PERMISSIONS = 1

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

        Utils(applicationContext).checkPermissions(this)
    }

    // Verifying request to notify the user
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSIONS) {
            var allPermissionsGranted = true

            for (grantResult in grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false
                    break
                }
            }

            if (!allPermissionsGranted) {
                Toast.makeText(
                    this,
                    "Permissions not granted",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun runExtractions() {
        // ToDo: Validar si esta seleccionado el checkbox, validar permisos, validar comandos
        val myAcquisition = Acquisition(this, applicationContext)
        myAcquisition.test()
        myAcquisition.initialize()
        myAcquisition.getBackup(contentResolver)
        myAcquisition.getProp()
        /*
        myAcquisition.getSettings()
        myAcquisition.getProcesses()
        myAcquisition.getServices()
        myAcquisition.getLogcat()
        myAcquisition.getLogs()
        myAcquisition.getDumpSys()
        myAcquisition.getPackages()
         */
    }

}