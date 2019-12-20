package com.qzc.extensionkit.ext

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import androidx.fragment.app.Fragment

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

fun androidx.fragment.app.Fragment.alert(init: AlertDialog.Builder.() -> Unit): AlertDialog {
    val builder = AlertDialog.Builder(context)
    val dialog = builder.apply { init() }.create()
    dialog.show()
    return dialog
}