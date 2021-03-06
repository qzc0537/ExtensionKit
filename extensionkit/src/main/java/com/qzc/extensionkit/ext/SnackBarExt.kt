package com.qzc.extensionkit.ext

import androidx.annotation.NonNull
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import android.view.View
import android.view.ViewGroup
import com.qzc.extensionkit.EkConfigs


/**
 * created by qzc at 2019/09/21 15:14
 * desc:
 */
fun makeSnackBar(view: View, @NonNull text: CharSequence, duration: Int): Snackbar {
    val snackBar= Snackbar.make(view, text, duration)
    snackBar.view.setBackgroundColor(EkConfigs.snackBarBgColor)
    snackBar.setActionTextColor(EkConfigs.snackBarTextColor)
    return snackBar
}

fun makeSnackBar(view: View, @StringRes resId: Int, duration: Int): Snackbar {
    return makeSnackBar(view, view.context.getString(resId), duration)
}

fun View.snackBar(@StringRes text: Int): Snackbar {
    val snackBar = makeSnackBar(this, text, Snackbar.LENGTH_SHORT)
    snackBar.show()
    return snackBar
}

fun View.snackBar(@NonNull text: CharSequence): Snackbar {
    val snackBar = makeSnackBar(this, text, Snackbar.LENGTH_SHORT)
    snackBar.show()
    return snackBar
}

fun View.snackBarLong(@StringRes text: Int): Snackbar {
    val snackBar = makeSnackBar(this, text, Snackbar.LENGTH_LONG)
    snackBar.show()
    return snackBar
}

fun View.snackBarLong(@NonNull text: CharSequence): Snackbar {
    val snackBar = makeSnackBar(this, text, Snackbar.LENGTH_LONG)
    snackBar.show()
    return snackBar
}

fun View.snackBarIndefinite(@StringRes text: Int): Snackbar {
    val snackBar = makeSnackBar(this, text, Snackbar.LENGTH_INDEFINITE)
    snackBar.show()
    return snackBar
}

fun View.snackBarIndefinite(@NonNull text: CharSequence): Snackbar {
    val snackBar = makeSnackBar(this, text, Snackbar.LENGTH_INDEFINITE)
    snackBar.show()
    return snackBar
}

fun ViewGroup.snackBar(@StringRes text: Int): Snackbar {
    val snackBar = makeSnackBar(this, text, Snackbar.LENGTH_SHORT)
    snackBar.show()
    return snackBar
}

fun ViewGroup.snackBar(@NonNull text: CharSequence): Snackbar {
    val snackBar = makeSnackBar(this, text, Snackbar.LENGTH_SHORT)
    snackBar.show()
    return snackBar
}

fun ViewGroup.snackBarLong(@StringRes text: Int): Snackbar {
    val snackBar = makeSnackBar(this, text, Snackbar.LENGTH_LONG)
    snackBar.show()
    return snackBar
}

fun ViewGroup.snackBarLong(@NonNull text: CharSequence): Snackbar {
    val snackBar = makeSnackBar(this, text, Snackbar.LENGTH_LONG)
    snackBar.show()
    return snackBar
}

fun ViewGroup.snackBarIndefinite(@StringRes text: Int): Snackbar {
    val snackBar = makeSnackBar(this, text, Snackbar.LENGTH_INDEFINITE)
    snackBar.show()
    return snackBar
}

fun ViewGroup.snackBarIndefinite(@NonNull text: CharSequence): Snackbar {
    val snackBar = makeSnackBar(this, text, Snackbar.LENGTH_INDEFINITE)
    snackBar.show()
    return snackBar
}