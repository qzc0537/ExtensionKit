package com.qzc.extensionkit.ext

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonParser

/**
 * created by qzc at 2019/10/22 10:21
 * desc:
 */

private val gson = Gson()

/**
 * 对象转Json字符串
 *
 * @param obj
 * @return
 */
fun objToJson(obj: Any): String {
    return gson.toJson(obj)
}

/**
 * 集合转Json字符串
 *
 * @param list
 * @return
 */
fun listToJson(list: List<*>): String {
    return gson.toJson(list)
}

/**
 * Json字符串转对象
 *
 * @param json
 * @param clazz
 * @param <T>
 * @return
</T> */
fun <T> jsonToObj(json: String, clazz: Class<T>): T {
    return gson.fromJson(json, clazz)
}

/**
 * Json字符串转集合
 *
 * @param json
 * @param clazz
 * @param <T>
 * @return
</T> */
fun <T> jsonToList(json: String, clazz: Class<T>): List<T> {
    if (TextUtils.isEmpty(json)) return ArrayList()
    val list = ArrayList<T>()
    val array = JsonParser().parse(json).asJsonArray
    for (element in array) {
        list.add(gson.fromJson(element, clazz))
    }
    return list
}