package com.penserbjorne.apkqf

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import java.io.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonRunExtraction: Button = findViewById(R.id.buttonRunExtraction)
        buttonRunExtraction.setOnClickListener {
            runExtractions()
        }
    }

    private fun runExtractions() {
        val resultTextView: TextView = findViewById(R.id.textViewResults)
        resultTextView.text = execCMD(arrayOf("sh", "-c", "getprop"))
        resultTextView.movementMethod = ScrollingMovementMethod()

        val resultTextView2: TextView = findViewById(R.id.textViewResults2)
        resultTextView2.text = execCMD(arrayOf("sh", "-c", "service list"))
        resultTextView2.movementMethod = ScrollingMovementMethod()
    }

    private fun execCMD (myCMD: Array<String>): String {
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
        } catch (e: IOException) {
            return e.toString()
        }
    }
}