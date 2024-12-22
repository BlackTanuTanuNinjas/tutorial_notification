package com.example.tutorialnotification

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.system.Os
import android.view.KeyEvent
import android.view.View
import com.example.tutorialnotification.databinding.ActivityMainBinding
import java.io.*
import java.util.*
import android.webkit.WebView

import android.webkit.WebViewClient
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : Activity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.browser.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                if (binding.browser.visibility != View.VISIBLE) {
                    binding.browser.visibility = View.VISIBLE
                    binding.splash.visibility = View.GONE
                    reportFullyDrawn()
                }
            }
        }

        if (bridge != null) {
            // This happens on re-creation of the activity e.g. after rotating the screen
            bridge!!.setWebView(binding.browser)
        } else {
            // This happens only on the first time when starting the app
            bridge = Bridge(applicationContext, binding.browser)
        }

        requestNotificationPermission()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (binding.browser.canGoBack()) {
                        binding.browser.goBack()
                    } else {
                        finish()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun requestNotificationPermission() {
        // Android 13以上で権限必要
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission = Manifest.permission.POST_NOTIFICATIONS

            if (ContextCompat.checkSelfPermission(this, notificationPermission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(notificationPermission),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            } else {
                println("通知権限は許可されている")
            }
        } else {
            println("通知権限の要求は不要")
        }
    }

    companion object {
        var bridge: Bridge? = null

        private const val REQUEST_NOTIFICATION_PERMISSION = 1001
    }
}
