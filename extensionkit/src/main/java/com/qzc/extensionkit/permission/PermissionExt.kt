package com.qzc.extensionkit.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.core.content.ContextCompat
import android.app.AppOpsManager
import android.app.NotificationManager
import androidx.annotation.RequiresApi
import com.qzc.extensionkit.ext.appOpsManager
import com.qzc.extensionkit.ext.notificationManager


/**
 * created by qzc at 2019/09/22 12:30
 * desc:
 */

const val TAG = "permission"

fun androidx.fragment.app.FragmentActivity.request(vararg permissions: String) {
    ActivityCompat.requestPermissions(this, permissions, 0XFF)
}

fun androidx.fragment.app.FragmentActivity.request(
    vararg permissions: String,
    callbacks: PermissionsCallbackDSL.() -> Unit
) {

    val permissionsCallback = PermissionsCallbackDSL().apply { callbacks() }
    val requestCode = PermissionsMap.put(permissionsCallback)

    val needRequestPermissions = permissions.filter { !isGranted(it) }

    if (needRequestPermissions.isEmpty()) {
        permissionsCallback.onGranted()
    } else {
        val shouldShowRationalePermissions = mutableListOf<String>()
        val shouldNotShowRationalePermissions = mutableListOf<String>()
        for (permission in needRequestPermissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                shouldShowRationalePermissions.add(permission)
            else
                shouldNotShowRationalePermissions.add(permission)
        }

        if (shouldShowRationalePermissions.isNotEmpty()) {
            permissionsCallback.onShowRationale(
                PermissionRequest(
                    getEkPermissionFragment(this),
                    shouldShowRationalePermissions,
                    requestCode
                )
            )
        }


        if (shouldNotShowRationalePermissions.isNotEmpty()) {
            getEkPermissionFragment(this).requestPermissionsByFragment(
                shouldNotShowRationalePermissions.toTypedArray(),
                requestCode
            )
        }
    }
}

private fun getEkPermissionFragment(activity: androidx.fragment.app.FragmentActivity): EkPermissionFragment {
    var fragment = activity.supportFragmentManager.findFragmentByTag(TAG)
    if (fragment == null) {
        fragment = EkPermissionFragment()
        activity.supportFragmentManager.beginTransaction().add(fragment, TAG).commitNow()
    }
    return fragment as EkPermissionFragment
}


fun Activity.isGranted(permission: String): Boolean {
    return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

@RequiresApi(Build.VERSION_CODES.KITKAT)
fun Context.isNotificationEnable(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //8.0手机以上
        if (notificationManager?.importance == NotificationManager.IMPORTANCE_NONE) {
            return false
        }
    }

    val CHECK_OP_NO_THROW = "checkOpNoThrow"
    val OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION"

    val mAppOps = appOpsManager
    val appInfo = applicationInfo
    val pkg = packageName
    val uid = appInfo.uid

    var appOpsClass: Class<*>? = null

    try {
        appOpsClass = Class.forName(AppOpsManager::class.java.name)
        val checkOpNoThrowMethod = appOpsClass!!.getMethod(
            CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
            String::class.java
        )
        val opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION)

        val value = opPostNotificationValue.get(Int::class.java) as Int
        return checkOpNoThrowMethod.invoke(
            mAppOps,
            value,
            uid,
            pkg
        ) as Int == AppOpsManager.MODE_ALLOWED

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return false
}