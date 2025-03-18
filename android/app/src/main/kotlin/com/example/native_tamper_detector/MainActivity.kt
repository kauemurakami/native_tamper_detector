package com.example.native_tamper_detector

import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import kotlin.system.exitProcess
import com.deebx.flutter_tamper_detector.RootChecker
import com.deebx.flutter_tamper_detector.HookDetector
import com.deebx.flutter_tamper_detector.IsEmulator
import com.deebx.flutter_tamper_detector.IsDebug

class MainActivity : FlutterActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // if (RootChecker.isDeviceRooted() || HookDetector.check() 
        //  ) {
        //     uninstallApp() // uninstall app
        // }
        if (RootChecker.isDeviceRooted() || HookDetector.check() || IsEmulator.check() 
        // ||IsDebug.check(this)
         ) {
            finish() // Closes the activity before terminating the process
            exitProcess(0) // Completely shuts down the application
        }
    }
    
    private fun uninstallApp() {
        try {
            // Attempt to uninstall via root ("su" command)
            val packageName = context.packageName
            val process = Runtime.getRuntime().exec("su")
            val outputStream = process.outputStream
            outputStream.write("pm uninstall $packageName\n".toByteArray())
            outputStream.flush()
            outputStream.close()

            // Check the uninstall result
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val result = reader.readLine()
            reader.close()


            // If uninstalling via root fails, try uninstalling normally
            if (result == null || !result.contains("Success")) {
                val intent = Intent(Intent.ACTION_DELETE)
                intent.data = Uri.parse("package:$packageName")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
        } catch (e: Exception) {
            e
        }
    }
}