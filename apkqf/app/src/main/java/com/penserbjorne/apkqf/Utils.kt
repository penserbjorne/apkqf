package com.penserbjorne.apkqf

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.IOError
import java.io.InputStreamReader

private const val acquisitionTAG = "apkqf utils"

class Utils(applicationContext: Context) {

    // Sharing applicationContext between classes
    private val myApplicationContext: Context = applicationContext

    fun test() {
        val testMSG = "Hello from Utils test"
        Log.d(acquisitionTAG, testMSG)
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

    fun saveFile(folderName: String, fileName: String, fileContent: String): String {
        // ToDO: Validacionde carpetas, sistema de archivos montado
        return try {
            val myFolder = myApplicationContext.getDir(folderName, Context.MODE_PRIVATE)
            val myFile = File(myFolder, fileName)
            myFile.outputStream().use { it.write(fileContent.toByteArray()) }

            if (myFile.exists()) {
                "$fileName created"
            } else {
                "$fileName NOT created"
            }
        } catch (e: Exception) {
            e.toString()
        }
    }
}