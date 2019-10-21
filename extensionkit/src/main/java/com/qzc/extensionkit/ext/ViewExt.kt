package com.qzc.extensionkit.ext

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import com.qzc.extensionkit.EkConfigs

/**
 * created by qzc at 2019/09/18 18:08
 * desc:
 */
private var sViewId: Int = 0
private var sLastTime: Long = 0L

fun View.onClick(time: Int = EkConfigs.repeatInTime, action: View.() -> Unit) {
    this.setOnClickListener {
        val viewId = this.id
        if (viewId == sViewId && System.currentTimeMillis() - sLastTime <= time) {
            return@setOnClickListener
        }
        sViewId = viewId
        sLastTime = System.currentTimeMillis()
        action.invoke(this)
    }
}

fun View.onClickUnit(time: Int = EkConfigs.repeatInTime, action: () -> Unit) {
    this.setOnClickListener {
        val viewId = this.id
        if (viewId == sViewId && System.currentTimeMillis() - sLastTime <= time) {
            return@setOnClickListener
        }
        sViewId = viewId
        sLastTime = System.currentTimeMillis()
        action.invoke()
    }
}

fun View.onClick(time: Int = EkConfigs.repeatInTime, l: View.OnClickListener) {
    this.setOnClickListener {
        val viewId = this.id
        if (viewId == sViewId && System.currentTimeMillis() - sLastTime <= time) {
            return@setOnClickListener
        }
        sViewId = viewId
        sLastTime = System.currentTimeMillis()
        this.setOnClickListener(l)
    }
}

fun View.onClick(l: View.OnClickListener) {
    onClick(EkConfigs.repeatInTime, l)
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

val Context.screenWidth
    get() = resources.displayMetrics.widthPixels

val Context.screenHeight
    get() = resources.displayMetrics.heightPixels

fun Context.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun Context.px2dp(px: Int): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

fun View.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun View.px2dp(px: Int): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

fun Context.string(id: Int): String {
    return this.getString(id)
}

fun Context.color(id: Int): Int {
    return ContextCompat.getColor(this, id)
}

fun Context.drawable(id: Int): Drawable {
    return ContextCompat.getDrawable(this, id)!!
}

fun Context.copyToClipboard(text: String, label: String = "ViewExt") {
    val clipData = ClipData.newPlainText(label, text)
    clipboardManager?.primaryClip = clipData
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

fun TextView.drawableLeft(resId: Int) {
    this.setCompoundDrawables(context.drawable(resId), null, null, null)
}

fun TextView.drawableTop(resId: Int) {
    this.setCompoundDrawables(null, context.drawable(resId), null, null)
}

fun TextView.drawableRight(resId: Int) {
    this.setCompoundDrawables(null, null, context.drawable(resId), null)
}

fun TextView.drawableBottom(resId: Int) {
    this.setCompoundDrawables(null, null, null, context.drawable(resId))
}

fun Button.drawableLeft(resId: Int) {
    this.setCompoundDrawables(context.drawable(resId), null, null, null)
}

fun Button.drawableTop(resId: Int) {
    this.setCompoundDrawables(null, context.drawable(resId), null, null)
}

fun Button.drawableRight(resId: Int) {
    this.setCompoundDrawables(null, null, context.drawable(resId), null)
}

fun Button.drawableBottom(resId: Int) {
    this.setCompoundDrawables(null, null, null, context.drawable(resId))
}

fun RadioButton.drawableLeft(resId: Int) {
    this.setCompoundDrawables(context.drawable(resId), null, null, null)
}

fun RadioButton.drawableTop(resId: Int) {
    this.setCompoundDrawables(null, context.drawable(resId), null, null)
}

fun RadioButton.drawableRight(resId: Int) {
    this.setCompoundDrawables(null, null, context.drawable(resId), null)
}

fun RadioButton.drawableBottom(resId: Int) {
    this.setCompoundDrawables(null, null, null, context.drawable(resId))
}

fun Activity.requestLandscape() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

fun Activity.requestPortrait() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}
