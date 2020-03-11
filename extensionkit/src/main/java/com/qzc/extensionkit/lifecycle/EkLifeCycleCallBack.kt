package com.qzc.extensionkit.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.qzc.extensionkit.EkConfigs
import com.qzc.extensionkit.ext.logi

/**
 * created by qzc at 2019/09/22 12:11
 * desc:
 */
class EkLifeCycleCallBack : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (EkConfigs.showActivityLife) logi("onActivityCreated : ${activity.localClassName}")
    }

    override fun onActivityStarted(activity: Activity) {
        if (EkConfigs.showActivityLife) logi("onActivityStarted : ${activity.localClassName}")
    }

    override fun onActivityResumed(activity: Activity) {
        if (EkConfigs.showActivityLife) logi("onActivityResumed : ${activity.localClassName}")
    }

    override fun onActivityPaused(activity: Activity) {
        if (EkConfigs.showActivityLife) logi("onActivityPaused : ${activity.localClassName}")
    }


    override fun onActivityDestroyed(activity: Activity) {
        if (EkConfigs.showActivityLife) logi("onActivityDestroyed : ${activity.localClassName}")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity) {
        if (EkConfigs.showActivityLife) logi("onActivityStopped : ${activity.localClassName}")
    }


}