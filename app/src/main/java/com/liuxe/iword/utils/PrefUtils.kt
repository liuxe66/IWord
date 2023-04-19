package com.liuxe.iword.utils

import android.annotation.SuppressLint
import com.tencent.mmkv.MMKV
import java.io.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 *  author : liuxe
 *  date : 2021/7/13 2:58 下午
 *  description :  mmvk替代sharePrefrence  进行数据本地化
 */
class PrefUtils<T>(val name: String, private val default: T) : ReadWriteProperty<Any?, T> {

    companion object {
        const val prefWordLevel = "preWordLevel"
        const val prefWordList = "preWordList"
    }

    private val mmkv: MMKV by lazy {
        MMKV.defaultMMKV()
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getValue(name, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putValue(name, value)
    }

    @SuppressLint("CommitPrefEdits")
    private fun <T> putValue(name: String, value: T) = with(mmkv) {
        when (value) {
            is Long -> encode(name, value)
            is String -> encode(name, value)
            is Int -> encode(name, value)
            is Boolean -> encode(name, value)
            is Float -> encode(name, value)
            else -> encode(name, serialize(value))
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getValue(name: String, default: T): T = with(mmkv) {
        val res: Any = when (default) {
            is Long -> decodeLong(name, default)
            is String -> decodeString(name, default) as String
            is Int -> decodeInt(name, default)
            is Boolean -> decodeBool(name, default)
            is Float -> decodeFloat(name, default)
            else -> deSerialization(decodeString(name, serialize(default)) as String)
        }
        return res as T
    }



    /**
     * 根据key删除存储数据
     */
    fun clearPreference(key: String) {
        mmkv.removeValueForKey(key)
    }

    /**
     * 序列化对象
     * @param person
     * *
     * @return
     * *
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun <A> serialize(obj: A): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(
            byteArrayOutputStream
        )
        objectOutputStream.writeObject(obj)
        var serStr = byteArrayOutputStream.toString("ISO-8859-1")
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8")
        objectOutputStream.close()
        byteArrayOutputStream.close()
        return serStr
    }

    /**
     * 反序列化对象
     * @param str
     * *
     * @return
     * *
     * @throws IOException
     * *
     * @throws ClassNotFoundException
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IOException::class, ClassNotFoundException::class)
    private fun <A> deSerialization(str: String): A {
        val redStr = java.net.URLDecoder.decode(str, "UTF-8")
        val byteArrayInputStream = ByteArrayInputStream(
            redStr.toByteArray(charset("ISO-8859-1"))
        )
        val objectInputStream = ObjectInputStream(
            byteArrayInputStream
        )
        val obj = objectInputStream.readObject() as A
        objectInputStream.close()
        byteArrayInputStream.close()
        return obj
    }


    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    fun contains(key: String): Boolean {
        return mmkv.contains(key)
    }

}