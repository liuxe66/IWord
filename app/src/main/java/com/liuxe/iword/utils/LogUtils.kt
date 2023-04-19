package com.liuxe.iword.utils

import com.orhanobut.logger.Logger

/**
 *  author : liuxe
 *  date : 2021/7/12 2:56 下午
 *  description :
 */
object LogUtils {

    fun d(msg: String) {
        Logger.d(msg)
    }

    fun i(msg: String) {
        Logger.i(msg)
    }

    fun w(msg: String) {
        Logger.w(msg)
    }

    fun e(msg: String) {
        Logger.e(msg)
    }

    fun json(json: String) {
        Logger.json(json)
    }

}