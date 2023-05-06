package com.penserbjorne.apkqf

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.IOError
import java.io.InputStreamReader

private const val acquisitionTAG = "apkqf utils"
private const val REQUEST_PERMISSIONS = 1

class Utils(applicationContext: Context) {

    // Sharing applicationContext between classes
    private val myApplicationContext: Context = applicationContext

    fun test() {
        val testMSG = "Hello from Utils test"
        Log.d(acquisitionTAG, testMSG)
    }

    fun checkPermissions(mainActivity: MainActivity): Boolean {
        val permissions = arrayOf(
            // Permissions for SMS Backup
            Manifest.permission.READ_SMS,
            //Manifest.permission.WRITE_EXTERNAL_STORAGE
            Manifest.permission.READ_PHONE_STATE
            //Manifest.permission.READ_SETTINGS, // Does this exist? D;
            //Manifest.permission.READ_SECURE_SETTINGS, // Does this exist? D;
            //Manifest.permission.WRITE_SETTINGS // This is not allowed for users
        )

        val permissionsToRequest = mutableListOf<String>()

        // Check if permissions are granted
        // If not then add to a list of permissions to request
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    myApplicationContext,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(permission)
            }
        }

        // Requesting permissions
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                mainActivity,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS
            )
            // If you don't have all the permissions, then you can not start the extraction
            return false
        }
        // If you have all the permissions, then you can start the extraction
        return true
    }

    fun execCMD(myCMD: Array<String>): String {
        // ToDo: Obtener salida de errores
        try {
            // Creating a process to execute the received command
            val myProcess = Runtime.getRuntime().exec(myCMD)

            // Reading the result
            val bufferedReader = BufferedReader(InputStreamReader(myProcess.inputStream))
            val logger = StringBuilder()
            var line: String? = ""

            while (line != null) {
                line = bufferedReader.readLine()
                if (line != null) {
                    logger.append(line + "\n")
                }
            }
            bufferedReader.close()
            return logger.toString()
        } catch (e: IOError) {
            return e.toString()
        }
    }

    fun saveFile(folderName: String, fileName: String, fileContent: String): Boolean {
        // ToDO: Validacionde carpetas, sistema de archivos montado
        return try {
            val myFolder = myApplicationContext.getDir(folderName, Context.MODE_PRIVATE)
            val myFile = File(myFolder, fileName)

            myFile.outputStream().use { it.write(fileContent.toByteArray()) }

            myFile.exists()
        } catch (e: Exception) {
            e.toString()
            false
        }
    }
}