package com.liuxe.iword.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.liuxe.iword.app.IWordApp
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import kotlin.reflect.KProperty

/**
 *  author : liuxe
 *  date : 2022/1/17 1:33 下午
 *  description :
 */
class Preference<T>() {
    private var keyName: String? = null
    private var defaultValue: T? = null

    companion object {
        const val isFirstLoad = "isFirstLoad"
        const val isFirstOpen = "isFirstOpen"
    }

    constructor(keyName: String,defaultValue: T) : this() {
        this.keyName = keyName
        this.defaultValue = defaultValue
    }

    private val prefs: SharedPreferences by lazy {
        IWordApp.context.getSharedPreferences("default", Context.MODE_PRIVATE)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T  {

        return findSharedPreference(keyName!!, defaultValue!!)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {

        putSharedPreferences(keyName!!, value)
    }


    /**
     * 查找数据 返回一个具体的对象
     * 没有查找到value 就返回默认的序列化对象，然后经过反序列化返回
     */
    @Suppress("UNCHECKED_CAST")
    private fun  findSharedPreference(name: String, default: T): T = with(prefs) {
        val res = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> getString(name, serialize(default))?.let { deSerialization(it) }
        }
        res as T
    }

    @SuppressLint("CommitPrefEdits")
    private fun  putSharedPreferences(name: String, value: T) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> putString(name, serialize(value))
        }.apply()
    }


    /**
     * 序列化对象
     */
    @Throws(Exception::class)
    private fun <T> serialize(obj: T): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(
            byteArrayOutputStream)
        objectOutputStream.writeObject(obj)
        var serStr = byteArrayOutputStream.toString("ISO-8859-1")
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8")
        objectOutputStream.close()
        byteArrayOutputStream.close()
        return serStr
    }

    /**
     * 反序列化对象
     */
    @Throws(Exception::class)
    private fun <T> deSerialization(str: String): T {
        val redStr = java.net.URLDecoder.decode(str, "UTF-8")
        val byteArrayInputStream = ByteArrayInputStream(
            redStr.toByteArray(charset("ISO-8859-1")))
        val objectInputStream = ObjectInputStream(
            byteArrayInputStream)
        val obj = objectInputStream.readObject() as T
        objectInputStream.close()
        byteArrayInputStream.close()
        return obj
    }


}