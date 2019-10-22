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
    var logTag: String = "LogExt"
    @JvmField
    var logEnable: Boolean = true
    @JvmField
    var toastUseSystem: Boolean = true
    @JvmField
    var toastGravity: Int = Gravity.BOTTOM
    @JvmField
    var toastXOffset: Int = 0
    @JvmField
    var toastYOffset: Int = 0
    @JvmField
    var snackBarBgColor: Int = 0
    @JvmField
    var snackBarTextColor: Int = 0
    //显示Activity生命周期
    @JvmField
    var showActivityLife: Boolean = true
    //控件重复点击时间
    @JvmField
    var repeatInTime: Int = 500

    /**
     * Application中初始化
     */
    @JvmStatic
    fun initToast(
        application: Application,
        useSystem: Boolean = toastUseSystem,
        style: IToastStyle? = null
    ) {
        ToastUtils.init(application)
        if (style != null) ToastUtils.initStyle(style)
        toastUseSystem = useSystem
    }

}