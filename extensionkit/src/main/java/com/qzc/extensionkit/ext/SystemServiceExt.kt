package com.qzc.extensionkit.ext

import android.app.*
import android.app.job.JobScheduler
import android.content.ClipboardManager
import android.content.Context
import android.hardware.SensorManager
import android.location.LocationManager
import android.media.AudioManager
import android.media.MediaRouter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.os.PowerManager
import android.os.Vibrator
import android.os.storage.StorageManager
import android.telephony.CarrierConfigManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import android.view.inputmethod.InputMethodManager

/**
 * created by qzc at 2019/09/18 16:09
 * desc:
 */

val Context.windowManager
    get() =
        applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
val Context.clipboardManager
    get() =
        applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
val Context.layoutInflater
    get() =
        applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
val Context.activityManager
    get() =
        applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
val Context.powerManager
    get() =
        applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
val Context.alarmManager
    get() =
        applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
val Context.notificationManager
    get() =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
val Context.keyguardManager
    get() =
        applicationContext.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
val Context.locationManager
    get() =
        applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
val Context.storageManager
    get() =
        applicationContext.getSystemService(Context.STORAGE_SERVICE) as StorageManager
val Context.vibrator get() = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
val Context.connectivityManager
    get() =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
val Context.wifiManager
    get() =
        applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
val Context.audioManager
    get() =
        applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
val Context.mediaRouter
    get() =
        applicationContext.getSystemService(Context.MEDIA_ROUTER_SERVICE) as MediaRouter
val Context.telephonyManager
    get() =
        applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
val Context.sensorManager
    get() =
        applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
val Context.inputMethodManager
    get() =
        applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
val Context.downloadManager
    get() =
        applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
val Context.batteryManager
    get() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            applicationContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        } else {
            null
        }
val Context.jobScheduler
    get() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        } else {
            null
        }
val Context.accessibilityManager
    get() =
        applicationContext.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager