package com.qzc.extensionkit.ext

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.support.v4.app.Fragment

/**
 * created by qzc at 2019/09/21 12:43
 * desc:
 */
fun Context.alert(init: AlertDialog.Builder.() -> Unit): AlertDialog {
    val builder = AlertDialog.Builder(this)
    val dialog = builder.apply { init() }.create()
    dialog.show()
    return dialog
}

fun Activity.alert(init: AlertDialog.Builder.() -> Unit): AlertDialog {
    val builder = AlertDialog.Builder(this)
    val dialog = builder.apply { init() }.create()
    dialog.show()
    return dialog
}

fun Fragment.alert(init: AlertDialog.Builder.() -> Unit): AlertDialog {
    val builder = AlertDialog.Builder(activity!!)
    val dialog = builder.apply { init() }.create()
    dialog.show()
    return dialog
}