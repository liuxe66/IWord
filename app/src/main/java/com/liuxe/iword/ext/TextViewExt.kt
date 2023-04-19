package com.liuxe.iword.ext

import android.graphics.Color
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.Size
import androidx.core.content.ContextCompat


fun TextView.string() = this.text.toString()
fun TextView.int() = this.string().run {

    when {
        string().isEmpty() -> 0
        contains(".") -> toFloat().toInt()
        else -> toInt()
    }
}

fun TextView.float() = this.string().toFloat()

/**
 * 设置颜色直接使用colors.xml中定义的颜色即可
 */
fun TextView.setColor(@ColorRes resId: Int) {
    this.setTextColor(ContextCompat.getColor(context, resId))
}

fun TextView.setColor(@Size(min = 4) colorStr: String) {
    this.setTextColor(Color.parseColor(colorStr))
}

fun TextView.drawableTop(@DrawableRes resId: Int) {

    ContextCompat.getDrawable(context, resId)?.let {
        it.setBounds(0, 0, it.minimumWidth, it.minimumHeight)
        this.setCompoundDrawables(null, it, null, null)
    }
}

fun TextView.drawableStart(@DrawableRes resId: Int?) {

    if (resId == null || resId == 0) {
        return
    }

    ContextCompat.getDrawable(context, resId)?.let {
        it.setBounds(0, 0, it.minimumWidth, it.minimumHeight)
        this.setCompoundDrawables(it, null, null, null)
    }
}

fun TextView.drawable(
        @DrawableRes start: Int = 0,
        @DrawableRes top: Int = 0,
        @DrawableRes end: Int = 0,
        @DrawableRes bottom: Int = 0
) {

    this.setCompoundDrawablesRelative(this.getDrawable(start)?.apply { setBounds(0, 0, minimumWidth, minimumHeight) },
            this.getDrawable(top)?.apply { setBounds(0, 0, minimumWidth, minimumHeight) },
            this.getDrawable(end)?.apply { setBounds(0, 0, minimumWidth, minimumHeight) },
            this.getDrawable(bottom)?.apply { setBounds(0, 0, minimumWidth, minimumHeight) })
}

fun TextView.drawableBottom(@DrawableRes resId: Int) {

    ContextCompat.getDrawable(context, resId)?.let {
        it.setBounds(0, 0, it.minimumWidth, it.minimumHeight)
        this.setCompoundDrawables(null, null, null, it)
    }
}

