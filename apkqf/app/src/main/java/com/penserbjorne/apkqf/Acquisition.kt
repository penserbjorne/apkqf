package com.penserbjorne.apkqf

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.os.SystemClock
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import java.io.IOException
import java.util.*

private const val TAG = "apkqf acquisition"
private const val NO_PERMISSIONS = "The application need all the permissions to continue"

class Acquisition(mainActivity: MainActivity, applicationContext: Context) {

    // Sharing applicationContext between classes
    private val myMainActivity: MainActivity = mainActivity
    private val myApplicationContext: Context = applicationContext
    private val myUtils = Utils(myApplicationContext)

    // Get the elapsed real time in nanoseconds since the device was last booted and it is divided
    // by 1000000 to convert to milliseconds
    private val started = SystemClock.elapsedRealtimeNanos() / 1000000

    // storagePath, the main working directory to store the information. This folder will be named
    // as the UUID.
    private val storagePath: String = UUID.randomUUID().toString()

    // logsPath, the store the retrieved logs. This folder will be inside the StoragePath folder
    // its name will be logs.
    private val logsPath: String = "logs"

    // apksPath, the store the retrieved apks. This folder will be inside the StoragePath folder
    // its name will be logs.
    private val apksPath: String = "apks"

    fun test() {
        val testMSG = "Hello from Acquisition test"
        Log.d(TAG, testMSG)
        myUtils.test()
    }

    fun initialize() {
        Log.d(TAG, "Acquisition values")
        Log.d(TAG, started.toString())
        Log.d(TAG, storagePath)
        Log.d(TAG, logsPath)
        Log.d(TAG, apksPath)
    }

    @SuppressLint("Range")
    fun getBackup(contentResolver: ContentResolver) {
        Log.d(TAG, "SMS Backup")

        // If you don't have all the permissions then you can not continue
        if(!myUtils.checkPermissions(myMainActivity)){
            Log.d(TAG, NO_PERMISSIONS)
            return
        }

        val smsUri = Telephony.Sms.CONTENT_URI

        val cursor = contentResolver.query(
            smsUri,
            arrayOf(
                Telephony.Sms._ID,
                Telephony.Sms.ADDRESS,
                Telephony.Sms.DATE,
                Telephony.Sms.BODY
            ),
            null,
            null,
            null
        )

        // Try to retrieve the messages
        if (cursor != null && cursor.moveToFirst()) {
            try {
                var smsContent = ""

                do {
                    val id = cursor.getString(cursor.getColumnIndex(Telephony.Sms._ID))
                    val address = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS))
                    val date = cursor.getString(cursor.getColumnIndex(Telephony.Sms.DATE))
                    val body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY))

                    smsContent += "$id, $address, $date, $body\n"
                } while (cursor.moveToNext())

                // We have the messages, try to save it
                val msgResult = "SMS backup completed and stored at sms.txt"
                val saveFileResponse = myUtils.saveFile(storagePath, "sms.txt", smsContent)

                if(saveFileResponse) {
                    Log.d(TAG, msgResult)

                    Toast.makeText(
                        myApplicationContext,
                        msgResult,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val msgResult = "Error creating SMS backup"
                    Log.d(TAG, msgResult)
                    Toast.makeText(
                        myApplicationContext,
                        msgResult,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: IOException) {
                val msgResult = "Error creating SMS backup"
                Log.d(TAG, msgResult)
                Toast.makeText(
                    myApplicationContext,
                    msgResult,
                    Toast.LENGTH_SHORT
                ).show()
            }

            cursor.close()

        } else {
            val msgResult = "No SMS found to backup"
            Log.d(TAG, msgResult)
            Toast.makeText(
                myApplicationContext,
                msgResult,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun getProp() {
        Log.d(TAG, "GetProp")
        Log.d(
            TAG, myUtils.saveFile(
                storagePath, "getprop.txt",
                myUtils.execCMD(arrayOf("sh", "-c", "getprop"))
            ).toString()
        )
    }

    fun getSettings() {
        Log.d(TAG, "Settings system")
        Log.d(
            TAG, myUtils.saveFile(
                storagePath, "settings_system.txt",
                myUtils.execCMD(arrayOf("sh", "-c", "settings list system"))
            ).toString()
        )

        Log.d(TAG, "Settings secure")
        Log.d(
            TAG, myUtils.saveFile(
                storagePath, "settings_secure.txt",
                myUtils.execCMD(arrayOf("sh", "-c", "settings list secure"))
            ).toString()
        )

        Log.d(TAG, "Settings global")
        Log.d(
            TAG, myUtils.saveFile(
                storagePath, "settings_global.txt",
                myUtils.execCMD(arrayOf("sh", "-c", "settings list global"))
            ).toString()
        )
    }

    fun getProcesses() {
        Log.d(TAG, "Processes")
        Log.d(
            TAG, myUtils.saveFile(
                storagePath, "ps.txt",
                myUtils.execCMD(arrayOf("sh", "-c", "ps"))
            ).toString()
        )
    }

    fun getServices() {
        Log.d(TAG, "Services")
        Log.d(
            TAG, myUtils.saveFile(
                storagePath, "services.txt",
                myUtils.execCMD(arrayOf("sh", "-c", "service list"))
            ).toString()
        )
    }

    fun getLogcat() {
        Log.d(TAG, "Logcat")
        Log.d(
            TAG, myUtils.saveFile(
                storagePath, "logcat.txt",
                myUtils.execCMD(arrayOf("sh", "-c", "logcat -d -b all"))
            ).toString()
        )
    }

    fun getLogs() {
        Log.d(TAG, "Logs")
        Log.d(TAG, myUtils.saveFile(storagePath, "logs.txt", "ToDo").toString())
    }

    fun getDumpSys() {
        Log.d(TAG, "DumpSys")
        Log.d(
            TAG, myUtils.saveFile(
                storagePath, "dumpsys.txt",
                myUtils.execCMD(arrayOf("sh", "-c", "dumpsys"))
            ).toString()
        )
    }

    fun getPackages() {
        Log.d(TAG, "Packages")
        Log.d(TAG, myUtils.saveFile(storagePath, "packages.txt", "ToDo").toString())
    }
}