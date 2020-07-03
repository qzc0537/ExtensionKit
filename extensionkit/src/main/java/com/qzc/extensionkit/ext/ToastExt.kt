package com.qzc.extensionkit.ext

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.hjq.toast.ToastUtils
import com.qzc.extensionkit.EkConfigs

/**
 * created by qzc at 2019/09/18 17:17
 * desc:
 */
private var mToast: Toast? = null

fun Context.toast(
    text: CharSequence,
    duration: Int = Toast.LENGTH_SHORT,
    gravity: Int = EkConfigs.toastGravity,
    xOffset: Int = EkConfigs.toastXOffset,
    yOffset: Int = EkConfigs.toastYOffset,
    view: View? = null
) {
    if (EkConfigs.toastUseSystem) {
        if (mToast == null) {
            mToast = Toast.makeText(this.applicationContext, text, duration)
        }
        if (view != null) {
            mToast?.view = view
        } else {
            if (TextUtils.isEmpty(text)) return
            mToast?.setText(text)
        }
        mToast?.duration = duration
        mToast?.setGravity(gravity, xOffset, yOffset)
        mToast?.show()
    } else {
        if (mToast == null) {
            mToast = ToastUtils.getToast()
        }
        if (view != null) {
            mToast?.view = view
        } else {
            if (TextUtils.isEmpty(text)) return
            mToast?.setText(text)
        }
        mToast?.duration = duration
        mToast?.setGravity(gravity, xOffset, yOffset)
        mToast?.show()
    }
}

fun Context.cancelToast() {
    if (mToast != null) {
        mToast?.cancel()
    }
}

fun Context.toast(
    text: Int,
    duration: Int = Toast.LENGTH_SHORT,
    gravity: Int = Gravity.NO_GRAVITY,
    xOffset: Int = EkConfigs.toastXOffset,
    yOffset: Int = EkConfigs.toastYOffset
) {
    toast(getString(text), duration, gravity, xOffset, yOffset)
}

fun Context.toast(text: Int, duration: Int, gravity: Int = EkConfigs.toastGravity) {
    toast(
        text, duration, gravity, EkConfigs.toastXOffset,
        if (EkConfigs.toastYOffset == 0) 40 else EkConfigs.toastYOffset
    )
}

fun Context.toast(text: CharSequence, duration: Int, gravity: Int = EkConfigs.toastGravity) {
    toast(
        text, duration, gravity, EkConfigs.toastXOffset,
        if (EkConfigs.toastYOffset == 0) 40 else EkConfigs.toastYOffset
    )
}

fun Context.toast(text: Int) {
    toast(text, Toast.LENGTH_SHORT)
}

fun Context.toast(text: CharSequence) {
    toast(text, Toast.LENGTH_SHORT)
}

fun Context.toastLong(text: Int) {
    toast(text, Toast.LENGTH_LONG)
}

fun Context.toastLong(text: CharSequence) {
    toast(text, Toast.LENGTH_LONG)
}

fun Context.toastTop(text: Int) {
    toast(text, Toast.LENGTH_SHORT, Gravity.TOP)
}

fun Context.toastTop(text: CharSequence) {
    toast(text, Toast.LENGTH_SHORT, Gravity.TOP)
}

fun Context.toastTopLong(text: Int) {
    toast(text, Toast.LENGTH_LONG, Gravity.TOP)
}

fun Context.toastTopLong(text: CharSequence) {
    toast(text, Toast.LENGTH_LONG, Gravity.TOP)
}

fun Context.toastCenter(text: Int) {
    toast(text, Toast.LENGTH_SHORT, Gravity.CENTER)
}

fun Context.toastCenter(text: CharSequence) {
    toast(text, Toast.LENGTH_SHORT, Gravity.CENTER)
}

fun Context.toastCenterLong(text: Int) {
    toast(text, Toast.LENGTH_LONG, Gravity.CENTER)
}

fun Context.toastCenterLong(text: CharSequence) {
    toast(text, Toast.LENGTH_LONG, Gravity.CENTER)
}

fun Context.toastBottom(text: Int) {
    toast(text, Toast.LENGTH_SHORT, Gravity.BOTTOM)
}

fun Context.toastBottom(text: CharSequence) {
    toast(text, Toast.LENGTH_SHORT, Gravity.BOTTOM)
}

fun Context.toastBottomLong(text: Int) {
    toast(text, Toast.LENGTH_LONG, Gravity.BOTTOM)
}

fun Context.toastBottomLong(text: CharSequence) {
    toast(text, Toast.LENGTH_LONG, Gravity.BOTTOM)
}

fun Context.toast(view: View) {
    toast(
        "", Toast.LENGTH_SHORT, EkConfigs.toastGravity, EkConfigs.toastXOffset,
        EkConfigs.toastYOffset, view
    )
}

fun Context.toastLong(view: View) {
    toast(
        "", Toast.LENGTH_LONG, EkConfigs.toastGravity, EkConfigs.toastXOffset,
        EkConfigs.toastYOffset, view
    )
}

fun Context.getView(): View {
    return ToastUtils.getView()
}