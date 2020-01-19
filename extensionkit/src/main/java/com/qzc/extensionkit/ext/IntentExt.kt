package com.qzc.extensionkit.ext

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import java.io.File


/**
 * created by qzc at 2019/09/22 11:16
 * desc:
 */

fun Context.getSettingsIntent(): Intent =
    Intent().apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        action = Settings.ACTION_SETTINGS
    }

/** 跳转到设置页面 */
fun Context.gotoSettingsPage() {
    startActivity(getSettingsIntent())
}

fun Context.getAppInfoIntent(packageName: String = this.packageName): Intent =
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }

/** 跳转到应用信息页面 */
fun Context.goToAppInfoPage(packageName: String = this.packageName) {
    startActivity(getAppInfoIntent(packageName))
}

fun Context.getDateAndTimeIntent(): Intent =
    Intent(Settings.ACTION_DATE_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        putExtra("packageName", packageName)
    }

/**
 * 跳转到日期和时间页面
 */
fun Context.goToDateAndTimePage() {
    startActivity(getDateAndTimeIntent())
}

fun Context.getLanguageIntent() =
    Intent(Settings.ACTION_LOCALE_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        putExtra("packageName", packageName)
    }

/**
 * 跳转到语言设置页面
 */
fun Context.goToLanguagePage() {
    startActivity(getLanguageIntent())
}

/**
 * 安装意图
 */
fun Context.getInstallIntent(apkFile: File): Intent? {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//        //适配Android Q,注意mFilePath是通过ContentResolver得到的，上述有相关代码
//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.setDataAndType(Uri.parse(apkFile.absolutePath), "application/vnd.android.package-archive")
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        startActivity(intent)
//        return intent
//    }

    if (!apkFile.exists()) return null
    val intent = Intent(Intent.ACTION_VIEW)
    val uri: Uri

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        uri = Uri.fromFile(apkFile)
    } else {
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val authority = "$packageName.fileprovider"
        uri = FileProvider.getUriForFile(this, authority, apkFile)
    }
    intent.setDataAndType(uri, "application/vnd.android.package-archive")
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    return intent
}

/**
 * need android.permission.REQUEST_INSTALL_PACKAGES after N
 */
fun Context.installApk(apkFile: File) {
    val intent = getInstallIntent(apkFile)
    intent?.run { startActivity(this) }
}

/** 跳转到无障碍服务设置页面 */
fun Context.goToAccessibilitySetting() =
    Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).run { startActivity(this) }

/** 浏览器打开指定网页 */
fun Context.openBrowser(url: String) {
    Intent(Intent.ACTION_VIEW, Uri.parse(url)).run { startActivity(this) }
}

/** 在应用商店中打开应用 */
fun Context.openInAppStore(packageName: String = this.packageName) {
    val intent = Intent(Intent.ACTION_VIEW)
    try {
        intent.data = Uri.parse("market://details?id=$packageName")
        startActivity(intent)
    } catch (ifPlayStoreNotInstalled: ActivityNotFoundException) {
        intent.data =
            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        startActivity(intent)
    }
}

/** 启动 app */
fun Context.openApp(packageName: String) =
    packageManager.getLaunchIntentForPackage(packageName)?.run { startActivity(this) }

/** 卸载 app */
fun Context.uninstallApp(packageName: String) {
    Intent(Intent.ACTION_DELETE).run {
        data = Uri.parse("package:$packageName")
        startActivity(this)
    }
}

/** 发送邮件 */
fun Context.sendEmail(email: String, subject: String?, text: String?) {
    Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email")).run {
        subject?.let { putExtra(Intent.EXTRA_SUBJECT, subject) }
        text?.let { putExtra(Intent.EXTRA_TEXT, text) }
        startActivity(this)
    }
}

/** 系统分享文本 **/
fun Context.shareText(title: String, content: String) {
    var shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND
    shareIntent.type = "text/plain"
    shareIntent.putExtra(Intent.EXTRA_TEXT, content)
    //切记需要使用Intent.createChooser，否则会出现别样的应用选择框，您可以试试
    shareIntent = Intent.createChooser(shareIntent, title)
    startActivity(shareIntent)
}

/** 系统分享图片 **/
fun Context.shareImage(title: String, drawable: Int) {
    //将mipmap中图片转换成Uri
    val imgUri =
        Uri.parse("android.resource://" + applicationContext.packageName + "/" + drawable)
    var shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND
    //其中imgUri为图片的标识符
    shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri)
    shareIntent.type = "image/*"
    //切记需要使用Intent.createChooser，否则会出现别样的应用选择框，您可以试试
    shareIntent = Intent.createChooser(shareIntent, title)
    startActivity(shareIntent)
}

/** 系统分享图片 **/
fun Context.shareImages(title: String, vararg drawables: Int) {
    val imgUris = ArrayList<Uri>()
    for (drawable in drawables) {
        val imgUri =
            Uri.parse("android.resource://" + applicationContext.packageName + "/" + drawable)
        imgUris.add(imgUri)
    }

    var shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND_MULTIPLE
    //其中fileUri为文件的标识符
    shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imgUris)
    shareIntent.type = "image/*"
    //切记需要使用Intent.createChooser，否则会出现别样的应用选择框，您可以试试
    shareIntent = Intent.createChooser(shareIntent, title)
    startActivity(shareIntent)
}


