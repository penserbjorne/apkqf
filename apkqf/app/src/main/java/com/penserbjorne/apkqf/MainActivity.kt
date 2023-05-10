package com.penserbjorne.apkqf

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
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
                binding.textViewResults.text = "Permissions not granted\n"
                binding.textViewResults.append("You need to allow all permissions")
            }
        }
    }

    private fun runExtractions() {
        // ToDo: Validar si esta seleccionado el checkbox, validar permisos, validar comandos
        binding.textViewResults.text = ""
        val myAcquisition = Acquisition(this, applicationContext)
        myAcquisition.test()
        if (myAcquisition.initialize()){
            binding.textViewResults.append(myAcquisition.getBackup(contentResolver) + "\n")
            binding.textViewResults.append(myAcquisition.getProp() + "\n")
            binding.textViewResults.append(myAcquisition.getSystemSettings() + "\n")
            binding.textViewResults.append(myAcquisition.getSecureSettings() + "\n")
            binding.textViewResults.append(myAcquisition.getGlobalSettings() + "\n")
            binding.textViewResults.append(myAcquisition.getProcesses() + "\n")
            binding.textViewResults.append(myAcquisition.getServices() + "\n")
            binding.textViewResults.append(myAcquisition.getLogcat() + "\n")
            binding.textViewResults.append(myAcquisition.getLogs() + "\n")
            binding.textViewResults.append(myAcquisition.getDumpSys() + "\n")
            /*

            myAcquisition.getPackages()
             */
        } else {
            Log.d(TAG, "You need to allow all permissions")
        }
    }

}