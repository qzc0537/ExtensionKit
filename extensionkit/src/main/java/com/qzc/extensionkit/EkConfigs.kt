package com.qzc.extensionkit

import android.app.Application
import android.view.Gravity
import com.hjq.toast.IToastStyle
import com.hjq.toast.ToastUtils

/**
 * created by qzc at 2019/09/18 16:11
 * desc:
 */
object EkConfigs {

    @JvmField
    var toastUseSystem: Boolean = false
    @JvmField
    var toastGravity: Int = Gravity.BOTTOM
    @JvmField
    var toastXOffset: Int = 0
    @JvmField
    var toastYOffset: Int = 0
    @JvmField
    var logTag: String = "LogExt"
    @JvmField
    var snackBarBgColor: Int = 0
    @JvmField
    var snackBarTextColor: Int = 0
    @JvmField
    var showActivityLife: Boolean = true

    /**
     * Application中初始化
     */
    @JvmStatic
    fun initToast(application: Application, useSystem: Boolean = false) {
        ToastUtils.init(application)
        toastUseSystem = useSystem
    }

    /**
     * Application中初始化
     */
    @JvmStatic
    fun initStyle(style: IToastStyle) {
        ToastUtils.initStyle(style)
    }
}