package com.qzc.extensionkit.permission

/**
 * created by qzc at 2019/09/22 12:30
 * desc:
 */
data class PermissionRequest(
    val permissionFragment: EkPermissionFragment,
    val permissions: List<String>,
    val requestCode: Int
) {

    fun retry() {
        permissionFragment.requestPermissionsByFragment(permissions.toTypedArray(), requestCode)
    }
}