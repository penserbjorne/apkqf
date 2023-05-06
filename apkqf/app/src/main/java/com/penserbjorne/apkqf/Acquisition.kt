package com.penserbjorne.apkqf

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.os.SystemClock
import android.provider.Telephony
import android.provider.Settings
import android.util.Log
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

    fun initialize(): Boolean {
        Log.d(TAG, "Acquisition values")
        Log.d(TAG, started.toString())
        Log.d(TAG, storagePath)
        Log.d(TAG, logsPath)
        Log.d(TAG, apksPath)

        return myUtils.checkPermissions(myMainActivity)
    }

    // ToDo: Need to fix the content format
    @SuppressLint("Range")
    fun getBackup(contentResolver: ContentResolver): String {
        Log.d(TAG, "SMS Backup")

        // If you don't have all the permissions then you can not continue
        if (!myUtils.checkPermissions(myMainActivity)) {
            Log.d(TAG, NO_PERMISSIONS)
            return ""
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
                val saveFileResponse = myUtils.saveFile(storagePath, "sms.txt", smsContent)

                val msgResult = if (saveFileResponse) {
                    "SMS backup completed and stored at sms.txt"
                } else {
                    "Error creating SMS backup"
                }

                cursor.close()

                Log.d(TAG, msgResult)
                return msgResult

            } catch (e: IOException) {
                val msgResult = "Error creating SMS backup"
                Log.d(TAG, msgResult)
                return msgResult
            }

        } else {
            val msgResult = "No SMS found to backup"
            Log.d(TAG, msgResult)
            return msgResult
        }
    }

    // I decide to do this through ADB as the class BUILD only retrieve the build properties
    // Maybe I can improve this in future with other classes
    fun getProp(): String {
        Log.d(TAG, "GetProp")

        // If you don't have all the permissions then you can not continue
        if (!myUtils.checkPermissions(myMainActivity)) {
            Log.d(TAG, NO_PERMISSIONS)
            return ""
        }

        val properties = myUtils.execCMD(arrayOf("sh", "-c", "getprop"))
        val saveFileResponse = myUtils.saveFile(storagePath, "getprop.txt", properties)

        val msgResult = if (saveFileResponse) {
            "Device properties extracted and stored at getprop.txt"
        } else {
            "Error extracting device properties"
        }

        Log.d(TAG, msgResult)
        return msgResult
    }

    // Retrieve the list of system settings
    // ToDo: Need to fix the content format
    @SuppressLint("Range")
    fun getSystemSettings(): String {
        Log.d(TAG, "Settings system")

        // If you don't have all the permissions then you can not continue
        if (!myUtils.checkPermissions(myMainActivity)) {
            Log.d(TAG, NO_PERMISSIONS)
            return ""
        }

        val systemSettings = sortedMapOf<String, String>()
        val cursor = myApplicationContext.contentResolver.query(
            Settings.System.CONTENT_URI,
            null,
            null,
            null,
            Settings.System.NAME + " ASC"
        )

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    val key = cursor.getString(cursor.getColumnIndex(Settings.System.NAME))
                    val value = cursor.getString(cursor.getColumnIndex(Settings.System.VALUE))

                    if (value == null) {
                        systemSettings[key] = "null"
                    } else {
                        systemSettings[key] = value
                    }

                } while (cursor.moveToNext())

                cursor.close()

                val saveFileResponse = myUtils.saveFile(
                    storagePath, "settings_system.txt", systemSettings.toString()
                )

                val msgResult = if (saveFileResponse) {
                    "System settings extracted and stored at settings_system.txt"
                } else {
                    "Error extracting system settings"
                }

                Log.d(TAG, msgResult)
                return msgResult
            } catch (e: IOException) {
                val msgResult = "Error extracting system settings"
                Log.d(TAG, msgResult)
                return msgResult
            }
        } else {
            val msgResult = "Error extracting system settings. No system settings found."
            Log.d(TAG, msgResult)
            return msgResult
        }
    }

    // Retrieve the list of secure settings
    // ToDo: Need to fix the content format
    @SuppressLint("Range")
    fun getSecureSettings(): String {
        Log.d(TAG, "Settings secure")

        // If you don't have all the permissions then you can not continue
        if (!myUtils.checkPermissions(myMainActivity)) {
            Log.d(TAG, NO_PERMISSIONS)
            return ""
        }

        val secureSettings = sortedMapOf<String, String>()
        val cursor = myApplicationContext.contentResolver.query(
            Settings.Secure.CONTENT_URI,
            null,
            null,
            null,
            Settings.Secure.NAME + " ASC"
        )

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    val key = cursor.getString(cursor.getColumnIndex(Settings.Secure.NAME))
                    val value = cursor.getString(cursor.getColumnIndex(Settings.Secure.VALUE))

                    if (value == null) {
                        secureSettings[key] = "null"
                    } else {
                        secureSettings[key] = value
                    }

                } while (cursor.moveToNext())

                cursor.close()

                val saveFileResponse = myUtils.saveFile(
                    storagePath, "settings_secure.txt", secureSettings.toString()
                )

                val msgResult = if (saveFileResponse) {
                    "Secure settings extracted and stored at settings_secure.txt"
                } else {
                    "Error extracting secure settings"
                }

                Log.d(TAG, msgResult)
                return msgResult
            } catch (e: IOException) {
                val msgResult = "Error extracting secure settings"
                Log.d(TAG, msgResult)
                return msgResult
            }
        } else {
            val msgResult = "Error extracting secure settings. No secure settings found."
            Log.d(TAG, msgResult)
            return msgResult
        }
    }

    // Retrieve the list of global settings
    // ToDo: Need to fix the content format
    @SuppressLint("Range")
    fun getGlobalSettings(): String {
        Log.d(TAG, "Settings global")

        // If you don't have all the permissions then you can not continue
        if (!myUtils.checkPermissions(myMainActivity)) {
            Log.d(TAG, NO_PERMISSIONS)
            return ""
        }

        val globalSettings = sortedMapOf<String, String>()
        val cursor = myApplicationContext.contentResolver.query(
            Settings.Global.CONTENT_URI,
            null,
            null,
            null,
            Settings.Global.NAME + " ASC"
        )

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    val key = cursor.getString(cursor.getColumnIndex(Settings.Global.NAME))
                    val value = cursor.getString(cursor.getColumnIndex(Settings.Global.VALUE))

                    if (value == null) {
                        globalSettings[key] = "null"
                    } else {
                        globalSettings[key] = value
                    }

                } while (cursor.moveToNext())

                cursor.close()

                val saveFileResponse = myUtils.saveFile(
                    storagePath, "settings_global.txt", globalSettings.toString()
                )

                val msgResult = if (saveFileResponse) {
                    "Global settings extracted and stored at settings_global.txt"
                } else {
                    "Error extracting global settings"
                }

                Log.d(TAG, msgResult)
                return msgResult
            } catch (e: IOException) {
                val msgResult = "Error extracting global settings"
                Log.d(TAG, msgResult)
                return msgResult
            }
        } else {
            val msgResult = "Error extracting global settings. No secure settings found."
            Log.d(TAG, msgResult)
            return msgResult
        }
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