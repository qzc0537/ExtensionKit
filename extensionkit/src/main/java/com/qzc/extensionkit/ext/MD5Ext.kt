package com.qzc.extensionkit.ext

import android.text.TextUtils
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * created by qzc at 2019/10/22 11:38
 * desc:
 */

/**
 * md5加密
 * @param plainText 待加密字符串
 * @return 加密后32位字符串
 */
fun getMd5(plainText: String): String {
    if (TextUtils.isEmpty(plainText)) return ""

    try {
        val md = MessageDigest.getInstance("MD5")
        md.update(plainText.toByteArray())
        val b = md.digest()

        var i: Int

        val buf = StringBuffer("")
        for (offset in b.indices) {
            i = b[offset].toInt()
            if (i < 0)
                i += 256
            if (i < 16)
                buf.append("0")
            buf.append(Integer.toHexString(i))
        }
        //32位加密
        return buf.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
        return ""
    }

}