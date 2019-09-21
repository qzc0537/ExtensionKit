package com.qzc.extensionkit.ext

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.support.annotation.RequiresApi

/**
 * created by qzc at 2019/09/21 00:52
 * desc:
 */

val after16 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
val after17 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
val after18 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
val after19 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
val afterL = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
val afterM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
val afterN = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
val afterO = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
val afterP = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

val phoneModel = Build.MODEL
val phoneBrand = Build.BRAND
val phoneManufacture = Build.MANUFACTURER
val phoneProduct = Build.PRODUCT
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
val phoneSupoortAbis = Build.SUPPORTED_ABIS
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
val phoneSupoort32BitAbis = Build.SUPPORTED_32_BIT_ABIS
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
val phoneSupoort64BitAbis = Build.SUPPORTED_64_BIT_ABIS

/**
 *是否主线程
 */
val isMainThread = Looper.getMainLooper() == Looper.myLooper()

/**
 * 是否主进程
 */
fun Context.isMainProcess(): Boolean {
    return applicationContext.packageName == getCurrProcessName()
}

/**
 * 获取当前进程名称
 */
fun Context.getCurrProcessName(): String {
    val manager =
        applicationContext.getSystemService(Service.ACTIVITY_SERVICE) as ActivityManager

    for (processInfo in manager.runningAppProcesses) {
        if (processInfo.pid == android.os.Process.myPid()) {
            return processInfo.processName
        }
    }
    return ""
}

/**
 * 得到软件版本号
 */
fun Context.getVersionCode(): Int {
    var versionCode = -1
    try {
        versionCode = packageManager.getPackageInfo(packageName, 0).versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return versionCode
}

/**
 * 得到软件显示版本信息
 */
fun Context.getVersionName(): String {
    var versionName = ""
    try {
        versionName = packageManager.getPackageInfo(packageName, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return versionName
}

