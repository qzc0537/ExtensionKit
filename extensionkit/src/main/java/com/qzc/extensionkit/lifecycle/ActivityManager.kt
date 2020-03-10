package com.qzc.extensionkit.lifecycle

import android.app.Activity
import java.lang.ref.WeakReference
import java.util.*

/**
 * created by qzc at 2019/09/22 12:13
 * desc:
 */
object ActivityManager {

    private val mActivityList = LinkedList<WeakReference<Activity>>()

    fun currentActivity(): Activity? {
        checkWeakReference()
        return if (!mActivityList.isEmpty()) {
            mActivityList.last().get()
        } else null
    }

    /**
     * 检查弱引用是否释放，若释放，则从栈中清理掉该元素
     */
    fun checkWeakReference() {
        // 使用迭代器进行安全删除
        val it = mActivityList.iterator()
        while (it.hasNext()) {
            val reference = it.next()
            val temp = reference.get()
            if (temp == null) {
                it.remove()
            }
        }
    }

    /**
     * 清除指定引用
     */
    fun removeRefrence(clazz: Class<*>, finish: Boolean) {
        if (clazz == null) return
        val it = mActivityList.iterator()
        while (it.hasNext()) {
            val reference = it.next()
            val temp = reference.get()
            if (temp == null) {
                it.remove()
                continue
            }
            if (temp.javaClass == clazz) {
                it.remove()
                if (finish) temp.finish()
            }
        }
    }

    fun addActivity(activity: Activity) {
        mActivityList.add(WeakReference(activity))
    }

    fun finishCurrentActivity() {
        currentActivity()?.let { removeRefrence(it.javaClass, true) }
    }

    fun finishActivity(activity: Activity) {
        removeRefrence(activity.javaClass, true)
    }

    fun finishActivity(clazz: Class<*>) {
        removeRefrence(clazz, true)
    }

    fun finishAllActivity() {
        for (activity in mActivityList) {
            removeRefrence(activity.javaClass, true)
        }
    }

    fun finishAllActivity(retain: Activity) {
        for (reference in mActivityList) {
            reference.get()?.let {
                if (it.javaClass != retain.javaClass) {
                    removeRefrence(it.javaClass, true)
                }
            }
        }
    }
}