package com.example.native_tamper_detector

import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import kotlin.system.exitProcess
import com.deebx.flutter_tamper_detector.RootChecker
import com.deebx.flutter_tamper_detector.HookDetector
import com.deebx.flutter_tamper_detector.IsEmulator

class MainActivity : FlutterActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (RootChecker.isDeviceRooted() || HookDetector.check() || IsEmulator.check()) {
            finish() // Fecha a atividade antes de encerrar o processo
            exitProcess(0) // Encerra completamente o aplicativo
        }
    }
}