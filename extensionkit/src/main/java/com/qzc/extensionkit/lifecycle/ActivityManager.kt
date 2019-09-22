package com.qzc.extensionkit.lifecycle

import android.app.Activity
import java.util.*

/**
 * created by qzc at 2019/09/22 12:13
 * desc:
 */
object ActivityManager {

    private val mActivityList = LinkedList<Activity>()

    val currentActivity: Activity?
        get() = mActivityList.last


    fun addActivity(activity: Activity) {
        if (mActivityList.contains(activity)) {
            if (mActivityList.last != activity) {
                mActivityList.remove(activity)
                mActivityList.add(activity)
            }
        } else {
            mActivityList.add(activity)
        }
    }

    fun removeActivity(activity: Activity) {
        mActivityList.remove(activity)
    }

    fun finishCurrentActivity() {
        currentActivity?.finish()
    }

    fun finishActivity(activity: Activity) {
        mActivityList.remove(activity)
        activity.finish()
    }

    fun finishActivity(clazz: Class<*>) {
        for (activity in mActivityList)
            if (activity.javaClass == clazz)
                activity.finish()
    }

    fun finishAllActivity() {
        for (activity in mActivityList)
            activity.finish()
    }

    fun finishAllActivity(retain: Activity) {
        for (activity in mActivityList)
            if (retain != activity) activity.finish()
    }
}