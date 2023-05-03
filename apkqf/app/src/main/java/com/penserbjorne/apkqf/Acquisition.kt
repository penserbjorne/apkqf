package com.penserbjorne.apkqf

import android.content.Context
import android.util.Log
import java.util.*

private const val TAG = "apkqf acquisition"

class Acquisition(applicationContext: Context) {

    // Sharing applicationContext between classes
    private val myApplicationContext: Context = applicationContext
    private val myUtils = Utils(myApplicationContext)

    // ToDo
    // private val started

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

    fun initialize(): Boolean {
        Log.d(TAG, "Acquisition values")
        Log.d(TAG, storagePath)
        Log.d(TAG, logsPath)
        Log.d(TAG, apksPath)

        Log.d(TAG, "Creating folders")

        return true
    }

    fun getBackup() {
        Log.d(TAG, "SMS Backup")
        Log.d(
            TAG, myUtils.saveFile(
                storagePath, "backupSMS.txt",
                myUtils.execCMD(arrayOf("sh", "-c", "backup com.android.providers.telephony"))
            )
        )
    }

    fun getProp() {
        Log.d(TAG, "GetProp")
        Log.d(
            TAG, myUtils.saveFile(
                storagePath, "getprop.txt",
                myUtils.execCMD(arrayOf("sh", "-c", "getprop"))
            )
        )
    }

    fun getSettings() {
        Log.d(TAG, "Settings system")
        Log.d(
            TAG, myUtils.saveFile(
                storagePath, "settings_system.txt",
                myUtils.execCMD(arrayOf("sh", "-c", "settings list system"))
            )
        )

        Log.d(TAG, "Settings secure")
        Log.d(
            TAG, myUtils.saveFile(
                storagePath, "settings_secure.txt",
                myUtils.execCMD(arrayOf("sh", "-c", "settings list secure"))
            )
        )

        Log.d(TAG, "Settings global")
        Log.d(
            TAG, myUtils.saveFile(
                storagePath, "settings_global.txt",
                myUtils.execCMD(arrayOf("sh", "-c", "settings list global"))
            )
        )
    }

    fun getProcesses() {
        Log.d(TAG, "Processes")
        Log.d(
            TAG, myUtils.saveFile(
                storagePath, "ps.txt",
                myUtils.execCMD(arrayOf("sh", "-c", "ps"))
            )
        )
    }

    fun getServices() {
        Log.d(TAG, "Services")
        Log.d(
            TAG, myUtils.saveFile(
                storagePath, "services.txt",
                myUtils.execCMD(arrayOf("sh", "-c", "service list"))
            )
        )
    }

    fun getLogcat() {
        Log.d(TAG, "Logcat")
        Log.d(
            TAG, myUtils.saveFile(
                storagePath, "logcat.txt",
                myUtils.execCMD(arrayOf("sh", "-c", "logcat -d -b all"))
            )
        )
    }

    fun getLogs() {
        Log.d(TAG, "Logs")
        Log.d(TAG, myUtils.saveFile(storagePath, "logs.txt", "ToDo"))
    }

    fun getDumpSys() {
        Log.d(TAG, "DumpSys")
        Log.d(
            TAG, myUtils.saveFile(
                storagePath, "dumpsys.txt",
                myUtils.execCMD(arrayOf("sh", "-c", "dumpsys"))
            )
        )
    }

    fun getPackages() {
        Log.d(TAG, "Packages")
        Log.d(TAG, myUtils.saveFile(storagePath, "packages.txt", "ToDo"))
    }
}