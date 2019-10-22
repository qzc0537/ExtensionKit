package com.qzc.extensionkit.ext

import android.content.Context
import android.telephony.TelephonyManager
import android.net.ConnectivityManager

/**
 * created by qzc at 2019/10/22 11:39
 * desc:
 */
const val NET_NO = "NO"
const val NET_WIFI = "WIFI"
const val NET_4G = "4G"
const val NET_3G = "3G"
const val NET_2G = "2G"
const val NET_UNKNOWN = "Unknown"

/**
 * 检查网络是否连接
 * @return true 已连接 false 未连接
 */
fun Context?.isNetworkAvailable(): Boolean {
    if (this == null) return false
    val connectivityManager = this.connectivityManager ?: return false
    val networkInfo = connectivityManager.activeNetworkInfo ?: return false
    return networkInfo.isConnected
}

/**
 * 判断是否wifi连接
 *
 * @return true/false
 */
fun Context?.isWifiConnected(): Boolean {
    if (this == null) return false
    val connectivityManager = this.connectivityManager ?: return false
    val networkInfo = connectivityManager.activeNetworkInfo ?: return false
    val networkInfoType = networkInfo.type
    if (networkInfoType == ConnectivityManager.TYPE_WIFI
        || networkInfoType == ConnectivityManager.TYPE_ETHERNET
    ) {
        return networkInfo.isConnected
    }
    return false
}

/**
 * 获取当前的网络状态
 * @return
 * 没有网络-NO
 * WIFI网络-WIFI
 * 4G网络-4G
 * 3G网络-3G
 * 2G网络-2G
 * 未知-Unknown
 */
fun Context?.getNetworkType(): String {
    if (this == null) return NET_UNKNOWN
    val connectivityManager = this.connectivityManager ?: return NET_UNKNOWN
    val networkInfo = connectivityManager.activeNetworkInfo ?: return NET_NO //无网络

    val nType = networkInfo.type
    if (nType == ConnectivityManager.TYPE_WIFI) {
        return NET_WIFI
    } else if (nType == ConnectivityManager.TYPE_MOBILE) {
        val nSubType = networkInfo.subtype
        val telephonyManager = this.telephonyManager ?: return NET_UNKNOWN
        return if (nSubType == TelephonyManager.NETWORK_TYPE_LTE && !telephonyManager.isNetworkRoaming) {
            NET_4G
        } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
            || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
            || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_A
            || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
            || nSubType == TelephonyManager.NETWORK_TYPE_HSUPA
            || nSubType == TelephonyManager.NETWORK_TYPE_HSPA
            || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_B
            || nSubType == TelephonyManager.NETWORK_TYPE_EHRPD
            || nSubType == TelephonyManager.NETWORK_TYPE_HSPAP && !telephonyManager.isNetworkRoaming
        ) {
            NET_3G
        } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
            || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
            || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
            || nSubType == TelephonyManager.NETWORK_TYPE_1xRTT
            || nSubType == TelephonyManager.NETWORK_TYPE_IDEN && !telephonyManager.isNetworkRoaming
        ) {
            NET_2G
        } else {
            NET_UNKNOWN
        }
    }

    return NET_UNKNOWN
}