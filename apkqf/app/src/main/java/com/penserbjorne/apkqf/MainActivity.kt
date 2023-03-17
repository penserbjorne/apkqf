package com.penserbjorne.apkqf

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.penserbjorne.apkqf.databinding.ActivityMainBinding
import java.io.*
import java.util.*

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

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Assign execution to button
        binding.buttonRunExtraction.setOnClickListener { runExtractions() }
    }

    private fun runExtractions() {
        // ToDo: Validar si esta seleccionado el checkbox, validar permisos, validar comandos
        val myUUID = UUID.randomUUID().toString()

        Log.d(TAG, "SMS Backup")
        Log.d(TAG, saveFile(myUUID, "backupSMS.txt",
            execCMD(arrayOf("sh", "-c", "backup com.android.providers.telephony"))
        ))

        Log.d(TAG, "GetProp")
        Log.d(TAG, saveFile(myUUID, "getprop.txt",
            execCMD(arrayOf("sh", "-c", "getprop"))
        ))

        Log.d(TAG, "Settings system")
        Log.d(TAG, saveFile(myUUID, "settings_system.txt",
            execCMD(arrayOf("sh", "-c", "settings list system"))
        ))

        Log.d(TAG, "Settings secure")
        Log.d(TAG, saveFile(myUUID, "settings_secure.txt",
            execCMD(arrayOf("sh", "-c", "settings list secure"))
        ))

        Log.d(TAG, "Settings global")
        Log.d(TAG, saveFile(myUUID, "settings_global.txt",
            execCMD(arrayOf("sh", "-c", "settings list global"))
        ))

        Log.d(TAG, "Processes")
        Log.d(TAG, saveFile(myUUID, "ps.txt",
            execCMD(arrayOf("sh", "-c", "ps"))
        ))

        Log.d(TAG, "Services")
        Log.d(TAG, saveFile(myUUID, "services.txt",
            execCMD(arrayOf("sh", "-c", "service list"))
        ))

        Log.d(TAG, "Logcat")
        Log.d(TAG, saveFile(myUUID, "logcat.txt",
            execCMD(arrayOf("sh", "-c", "logcat -d -b all"))
        ))

        Log.d(TAG, "Logs")
        // ToDo

        Log.d(TAG, "DumpSys")
        Log.d(TAG, saveFile(myUUID, "dumpsys.txt",
            execCMD(arrayOf("sh", "-c", "dumpsys"))
        ))

        Log.d(TAG, "Packages")
        // ToDo
    }

    private fun execCMD (myCMD: Array<String>): String {
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
                if(line != null) {
                    logger.append(line + "\n")
                }
            }
            bufferedReader.close()
            return logger.toString()
        } catch (e: IOError) {
            return e.toString()
        }
    }

    private fun saveFile(folderName: String, fileName: String, fileContent: String): String{
        // ToDO: Validacionde carpetas, sistema de archivos montado
        return try {
            val myFolderUUID = applicationContext.getDir(folderName, Context.MODE_PRIVATE)
            val myFile = File(myFolderUUID, fileName)
            myFile.outputStream().use { it.write(fileContent.toByteArray()) }

            if(myFile.exists()){
                "$fileName created"
            }else{
                "$fileName NOT created"
            }
        }catch (e: Exception){
            e.toString()
        }
    }
}