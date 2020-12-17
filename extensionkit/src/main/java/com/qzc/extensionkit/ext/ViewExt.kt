package com.qzc.extensionkit.ext

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.qzc.extensionkit.EkConfigs

/**
 * created by qzc at 2019/09/18 18:08
 * desc:
 */
private var sLastTime: Long = 0L

@Synchronized
fun View?.onClick(time: Int = EkConfigs.repeatInTime, action: View.() -> Unit) {
    if (this == null) return
    this.setOnClickListener {
        if (System.currentTimeMillis() - sLastTime <= time) {
            return@setOnClickListener
        }
        sLastTime = System.currentTimeMillis()
        action.invoke(this)
    }
}

@Synchronized
fun View?.onClickUnit(time: Int = EkConfigs.repeatInTime, action: () -> Unit) {
    if (this == null) return
    this.setOnClickListener {
        if (System.currentTimeMillis() - sLastTime <= time) {
            return@setOnClickListener
        }
        sLastTime = System.currentTimeMillis()
        action.invoke()
    }
}

@Synchronized
fun View?.onClick(time: Int = EkConfigs.repeatInTime, l: View.OnClickListener) {
    if (this == null) return
    this.setOnClickListener {
        if (System.currentTimeMillis() - sLastTime <= time) {
            return@setOnClickListener
        }
        sLastTime = System.currentTimeMillis()
        this.setOnClickListener(l)
    }
}

@Synchronized
fun View?.onClick(l: View.OnClickListener) {
    if (this == null) return
    onClick(EkConfigs.repeatInTime, l)
}

@Synchronized
fun View?.isFastClick(period: Int = EkConfigs.repeatInTime): Boolean {
    if (this == null) return false
    if (System.currentTimeMillis() - sLastTime <= period) {
        return true
    }
    sLastTime = System.currentTimeMillis()
    return false
}

fun View?.visible() {
    if (this == null) return
    visibility = View.VISIBLE
}

fun View?.invisible() {
    if (this == null) return
    visibility = View.INVISIBLE
}

fun View?.gone() {
    if (this == null) return
    visibility = View.GONE
}

val Context?.screenWidth
    get() = Resources.getSystem().displayMetrics.widthPixels

val Context?.screenHeight
    get() = Resources.getSystem().displayMetrics.heightPixels

val Fragment?.screenWidth
    get() = Resources.getSystem().displayMetrics.widthPixels

val Fragment?.screenHeight
    get() = Resources.getSystem().displayMetrics.heightPixels

fun Context?.dp2px(dp: Float): Float {
    val scale = Resources.getSystem().displayMetrics.density
    return dp * scale + 0.5f
}

fun Context?.px2dp(px: Float): Float {
    val scale = Resources.getSystem().displayMetrics.density
    return px / scale + 0.5f
}

fun Context?.sp2px(value: Float): Float {
    val scale = Resources.getSystem().displayMetrics.scaledDensity
    return value * scale + 0.5f
}

fun Context?.px2sp(value: Float): Float {
    val scale = Resources.getSystem().displayMetrics.scaledDensity
    return value / scale + 0.5f
}

fun Fragment?.dp2px(dp: Float): Float {
    val scale = Resources.getSystem().displayMetrics.density
    return dp * scale + 0.5f
}

fun Fragment?.px2dp(px: Float): Float {
    val scale = Resources.getSystem().displayMetrics.density
    return px / scale + 0.5f
}

fun Fragment?.sp2px(value: Float): Float {
    val scale = Resources.getSystem().displayMetrics.scaledDensity
    return value * scale + 0.5f
}

fun Fragment?.px2sp(value: Float): Float {
    val scale = Resources.getSystem().displayMetrics.scaledDensity
    return value / scale + 0.5f
}

fun Context?.string(id: Int): String {
    if (this == null) return ""
    return this.getString(id)
}

fun Context?.color(id: Int): Int {
    if (this == null) return 0
    return ContextCompat.getColor(this, id)
}

fun Context?.drawable(id: Int): Drawable? {
    if (this == null) return null
    return ContextCompat.getDrawable(this, id)
}

fun Context?.copyToClipboard(text: String, label: String = "ViewExt") {
    if (this == null) return
    val clipData = ClipData.newPlainText(label, text)
    clipboardManager?.primaryClip = clipData
}

fun Fragment?.string(id: Int): String {
    if (this == null) return ""
    return this.getString(id)
}

fun Fragment?.color(id: Int): Int {
    if (this == null) return 0
    return ContextCompat.getColor(context!!, id)
}

fun Fragment.drawable(id: Int): Drawable? {
    if (context == null) return null
    return ContextCompat.getDrawable(context!!, id)
}

fun Fragment?.copyToClipboard(text: String, label: String = "ViewExt") {
    if (this == null) return
    val clipData = ClipData.newPlainText(label, text)
    context?.clipboardManager?.primaryClip = clipData
}

fun CharSequence?.notNullEmpty(): Boolean {
    return this != null && this.isNotEmpty()
}

fun CharSequence?.nullOrEmpty(): Boolean {
    return this == null || this.isEmpty()
}

inline fun <T> T?.notNull(yes: T.() -> Unit, no: () -> Unit) {
    return if (this != null) yes() else no()
}

inline fun <T, R> T?.notNull(block: T.() -> R): R? = this?.block()

fun TextView?.drawableLeft(resId: Int) {
    if (this == null) return
    this.setCompoundDrawables(context.drawable(resId), null, null, null)
}

fun TextView?.drawableTop(resId: Int) {
    if (this == null) return
    this.setCompoundDrawables(null, context.drawable(resId), null, null)
}

fun TextView?.drawableRight(resId: Int) {
    if (this == null) return
    this.setCompoundDrawables(null, null, context.drawable(resId), null)
}

fun TextView?.drawableBottom(resId: Int) {
    if (this == null) return
    this.setCompoundDrawables(null, null, null, context.drawable(resId))
}

fun Button?.drawableLeft(resId: Int) {
    if (this == null) return
    this.setCompoundDrawables(context.drawable(resId), null, null, null)
}

fun Button?.drawableTop(resId: Int) {
    if (this == null) return
    this.setCompoundDrawables(null, context.drawable(resId), null, null)
}

fun Button?.drawableRight(resId: Int) {
    if (this == null) return
    this.setCompoundDrawables(null, null, context.drawable(resId), null)
}

fun Button?.drawableBottom(resId: Int) {
    if (this == null) return
    this.setCompoundDrawables(null, null, null, context.drawable(resId))
}

fun RadioButton?.drawableLeft(resId: Int) {
    if (this == null) return
    this.setCompoundDrawables(context.drawable(resId), null, null, null)
}

fun RadioButton?.drawableTop(resId: Int) {
    if (this == null) return
    this.setCompoundDrawables(null, context.drawable(resId), null, null)
}

fun RadioButton?.drawableRight(resId: Int) {
    if (this == null) return
    this.setCompoundDrawables(null, null, context.drawable(resId), null)
}

fun RadioButton?.drawableBottom(resId: Int) {
    if (this == null) return
    this.setCompoundDrawables(null, null, null, context.drawable(resId))
}

fun Activity?.requestLandscape() {
    if (this == null) return
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

fun Activity?.requestPortrait() {
    if (this == null) return
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

fun Activity?.fullScreen() {
    if (this == null) return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 全屏显示，隐藏状态栏和导航栏，拉出状态栏和导航栏显示一会儿后消失。
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        } else {
            // 全屏显示，隐藏状态栏
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }
}
