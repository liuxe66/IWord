package com.liuxe.iword.ext

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat


fun View?.gone() {
    this?.visibility = View.GONE
}

fun View?.visible() {

    if (this?.isVisible() == true) {
        return
    }

    this?.visibility = View.VISIBLE
}

fun View?.invisible() {
    this?.visibility = View.INVISIBLE
}

/**
 * @param isGone true隐藏，false显示，隐藏是完全隐藏，位置发生变动，被依赖的控件也会发生位移
 */
fun View.isGone(isGone: Boolean) = if (isGone) {
    gone()
} else {
    visible()
}

fun View.isGone() = visibility == View.GONE

fun View.isVisible() = visibility == View.VISIBLE

/**
 * 只要有view就可以获取颜色，不需要找context了
 */
fun View.getColor(@ColorRes id: Int) = ContextCompat.getColor(context, id)

@Nullable
fun View.getDrawable(@DrawableRes id: Int): Drawable? {
    if (id == 0) {
        return null
    }
    return ContextCompat.getDrawable(context, id)
}

fun View.getString(@StringRes id: Int) = context.getString(id)