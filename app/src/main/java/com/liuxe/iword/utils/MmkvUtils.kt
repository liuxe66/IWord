package com.liuxe.iword.utils

/**
 *  author : liuxe
 *  date : 2023/4/24 17:37
 *  description :
 */
import android.os.Parcelable
import com.tencent.mmkv.MMKV


object MmkvUtils {

    var mv: MMKV? = null

    init {
        mv = MMKV.defaultMMKV()
    }

    fun encode(key: String?, `object`: Any) {
        when (`object`) {
            is String -> {
                mv?.encode(key, `object`)
            }
            is Int -> {
                mv?.encode(key, `object`)
            }
            is Boolean -> {
                mv?.encode(key, `object`)
            }
            is Float -> {
                mv?.encode(key, `object`)
            }
            is Long -> {
                mv?.encode(key, `object`)
            }
            is Double -> {
                mv?.encode(key, `object`)
            }
            is ByteArray -> {
                mv?.encode(key, `object`)
            }
            else -> {
                mv?.encode(key, `object`.toString())
            }
        }
    }

    fun encodeSet(key: String?, sets: Set<String?>?) {
        mv?.encode(key, sets)
    }

    fun encodeParcelable(key: String?, obj: Parcelable?) {
        mv?.encode(key, obj)
    }

    fun decodeInt(key: String?): Int? {
        return mv?.decodeInt(key, 0)
    }

    fun decodeDouble(key: String?): Double {
        return mv?.decodeDouble(key, 0.00)!!
    }

    fun decodeLong(key: String?): Long {
        return mv?.decodeLong(key, 0L)!!
    }

    fun decodeBoolean(key: String?): Boolean {
        return mv?.decodeBool(key, false) == true
    }

    fun decodeBooleanTure(key: String?, defaultValue: Boolean): Boolean {
        return mv?.decodeBool(key, defaultValue) == true
    }

    fun decodeFloat(key: String?): Float {
        return mv?.decodeFloat(key, 0f)!!
    }

    fun decodeBytes(key: String?): ByteArray {
        return mv?.decodeBytes(key)!!
    }

    fun decodeString(key: String?): String {
        return mv?.decodeString(key, "").toString()
    }

    fun decodeStringDef(key: String?, defaultValue: String): String {
        return mv?.decodeString(key, defaultValue).toString()
    }


    fun decodeStringSet(key: String?): Set<String> {
        return mv?.decodeStringSet(key, emptySet()) as Set<String>
    }

    fun <T : Parcelable?> decodeParcelable(key: String?, tClass: Class<T>?): T? {
        return mv?.decodeParcelable(key, tClass)
    }
}