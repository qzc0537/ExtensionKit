package com.qzc.extensionkit.permission

import java.util.concurrent.atomic.AtomicInteger

/**
 * created by qzc at 2019/09/22 12:30
 * desc:
 */
internal object PermissionsMap {

    private val atomicInteger = AtomicInteger(100)

    private val map = mutableMapOf<Int, PermissionsCallback>()

    fun put(callbacks: PermissionsCallback): Int {
        return atomicInteger.getAndIncrement().also {
            map[it] = callbacks
        }
    }

    fun get(requestCode: Int): PermissionsCallback? {
        return map[requestCode].also {
            map.remove(requestCode)
        }
    }

}