package com.liuxe.iword.app

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.annotation.Nullable
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGASoundManager
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.LogStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.tencent.mmkv.MMKV
import kotlin.properties.Delegates


/**
 *  author : liuxe
 *  date : 5/17/21 2:28 PM
 *  description :
 */
class IWordApp : Application() {
    companion object {
        var context: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        //mmkv 替代 shareprefresh
        MMKV.initialize(context)
        //log日志 Logger
        initLogger()

        SVGAParser.shareParser().init(this)
        // 初始化音频管理器，方便管理音频播放
        // 如果没有初始化，则默认按照原有方式加载音频
        SVGASoundManager.init()

        // 释放音频资源
        SVGASoundManager.release()
        SVGASoundManager.setVolume(0f)
    }

    /**
     * 初始化日志框架
     */
    private fun initLogger() {
        val logStrategy = object : LogStrategy {

            private val prefix = arrayOf(". ", " .")
            private var index = 0

            override fun log(priority: Int, tag: String?, message: String) {
                index = index xor 1
                Log.println(priority, prefix[index] + tag!!, message)
            }
        }

        val formatStrategy = PrettyFormatStrategy.newBuilder().logStrategy(logStrategy)
            .showThreadInfo(false)   // （可选）是否显示线程信息。 默认值为true
//            .methodCount(2)         // （可选）要显示的方法行数。 默认2
//            .methodOffset(7)        // （可选）隐藏内部方法调用到偏移量。 默认5
            .tag("redcat")         // （可选）每个日志的全局标记。 默认PRETTY_LOGGER
            .build()

        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, @Nullable tag: String?): Boolean {
                return true
            }
        })

    }
}