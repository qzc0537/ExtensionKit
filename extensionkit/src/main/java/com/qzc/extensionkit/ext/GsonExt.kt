package com.qzc.extensionkit.ext

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

/**
 * created by qzc at 2019/10/22 10:21
 * desc:
 */

private val gson = Gson()

fun objToJson(obj: Any): String {
    return gson.toJson(obj)
}

fun mapToJson(map: Map<String, Any>): String {
    return gson.toJson(map)
}

fun listToJson(list: List<*>): String {
    return gson.toJson(list)
}

fun <T> jsonToObj(json: String, clazz: Class<T>): T {
    return gson.fromJson(json, clazz)
}

fun <T> jsonToList(json: String, cls: Class<T>): List<T> {
    val list = ArrayList<T>()
    val array = JsonParser().parse(json).asJsonArray
    for (elem in array) {
        list.add(gson.fromJson(elem, cls))
    }
    return list
}

fun <T> jsonToListMaps(json: String): List<Map<String, T>>? {
    var list: List<Map<String, T>>? = null
    if (gson != null) {
        list = gson.fromJson<List<Map<String, T>>>(
            json,
            object : TypeToken<List<Map<String, T>>>() {

            }.type
        )
    }
    return list
}