package com.qzc.extensionkit.ext

import android.util.Log
import com.qzc.extensionkit.BuildConfig
import com.qzc.extensionkit.EkConfigs

/**
 * created by qzc at 2019/09/18 16:11
 * desc:
 */
const val V = "V"
const val D = "D"
const val I = "I"
const val W = "W"
const val E = "E"

fun log(level: String, tag: String = EkConfigs.logTag, msg: String) {
    if (!EkConfigs.logEnable) return
    when (level) {
        V -> Log.v(tag, msg)
        D -> Log.d(tag, msg)
        I -> Log.i(tag, msg)
        W -> Log.w(tag, msg)
        E -> Log.e(tag, msg)
    }
}

fun logv(tag: String, msg: String) {
    log(V, tag, msg)
}

fun logd(tag: String, msg: String) {
    log(D, tag, msg)
}

fun logi(tag: String, msg: String) {
    log(I, tag, msg)
}

fun logw(tag: String, msg: String) {
    log(W, tag, msg)
}

fun loge(tag: String, msg: String) {
    log(E, tag, msg)
}

fun logv(msg: String) {
    log(V, EkConfigs.logTag, msg)
}

fun logd(msg: String) {
    log(D, EkConfigs.logTag, msg)
}

fun logi(msg: String) {
    log(I, EkConfigs.logTag, msg)
}

fun logw(msg: String) {
    log(W, EkConfigs.logTag, msg)
}

fun loge(msg: String) {
    log(E, EkConfigs.logTag, msg)
}

