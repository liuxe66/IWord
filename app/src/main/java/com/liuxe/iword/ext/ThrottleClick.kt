package com.liuxe.iword.ext

import android.view.View
import java.util.concurrent.TimeUnit

fun View.throttleClick(
    interval: Long = 200,
    unit: TimeUnit = TimeUnit.MILLISECONDS,
    block: View.() -> Unit
) {
    setOnClickListener(ThrottleClickListener(interval, unit, block))
}

internal class ThrottleClickListener(
    private val interval: Long = 200,
    private val unit: TimeUnit = TimeUnit.MILLISECONDS,
    private var block: View.() -> Unit
) : View.OnClickListener {

    private var lastTime: Long = 0

    override fun onClick(v: View) {

        val currentTime = System.currentTimeMillis()

        if (currentTime - lastTime > unit.toMillis(interval)) {
            lastTime = currentTime
            v.animate().scaleY(0.9f).scaleX(0.9f).setDuration(100).withEndAction {
                v.animate().scaleY(1.0f).scaleX(1.0f).setDuration(100).withEndAction {
                    block(v)
                }
            }
        }

    }
}