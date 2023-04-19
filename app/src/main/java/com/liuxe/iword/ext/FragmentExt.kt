package com.liuxe.iword.ext

import android.app.Activity
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.view.Display
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.DialogFragment

/**
 * 下面两个跳转activity的方法，一个是结束自己一个不结束自己
 * ，省略掉this参数，kotlin写this太长了，而且有时候编译器还不提示的。。。
 *
 * @param block 在这里携带intent参数
 */
inline fun <reified T> Fragment.launchActivity(block: Intent.() -> Unit = {}) {
    activity?.let { it.startActivity(Intent(it, T::class.java).apply { block() }) }
}

fun Fragment.launchActivity(action: String, block: Intent.() -> Unit = {}) {
    startActivity(Intent().apply {
        setAction(action)
        block()
    })
}

inline fun <reified T> Fragment.toActivity(block: Intent.() -> Unit = {}) {
    activity?.let {
        it.startActivity(Intent(it, T::class.java).apply { block() })
        it.onBackPressed()
    }
}

/**
 * 带着过度动画启动
 */
inline fun <reified T : Activity> Fragment.launchActivity(vararg sharedElements: Pair<View, String>, block: Intent.() -> Unit = {}) {
    activity?.let {
        ActivityCompat.startActivity(
                it,
                Intent(it, T::class.java).apply { block() },
                ActivityOptionsCompat.makeSceneTransitionAnimation(it, *sharedElements).toBundle()
        )
    }
}

inline fun <reified T> Fragment.launchActivityForResult(requestCode: Int, block: Intent.() -> Unit = {}) {

    activity?.let {
        it.startActivityForResult(Intent(it, T::class.java).apply {
            block()
        }, requestCode)
    }
}

/**
 * 取得虚拟导航栏高度
 */
fun Fragment.getNavigationBarHeight(): Int {


    var nvaHeight = 0

    activity?.let {
        val display: Display = it.windowManager.defaultDisplay

        val size = Point()
        val realSize = Point()
        display.getSize(size)
        display.getRealSize(realSize)
        val resourceId: Int = Resources.getSystem().getIdentifier("navigation_bar_height", "dimen", "android")
        val height: Int = Resources.getSystem().getDimensionPixelSize(resourceId)
        //超出系统默认的导航栏高度以上，则认为存在虚拟导航
        nvaHeight = if (realSize.y - size.y > height - 10) {
            height
        } else 0
    }
    return nvaHeight
}

fun Fragment.isCutoutDisplay(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val windowInsets = activity?.window?.decorView?.rootWindowInsets?.displayCutout
        windowInsets != null
    } else
        false
}

fun Fragment.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android")

    if (resourceId > 0) {
        result = Resources.getSystem().getDimensionPixelSize(resourceId)
    }
    return result
}

/**
 * 隐藏输入法
 */
fun Fragment.hindKeyBroad() {
    activity?.let {
        val inputMethodManager = it.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(it.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}


fun Fragment.stringExtra(key: String, defValue: String = "") =
        arguments?.getString(key, defValue) ?: defValue

fun Fragment.intExtra(key: String, def: Int = -1) = arguments?.getInt(key, def) ?: def
fun Fragment.longExtra(key: String, def: Long = -1L) = arguments?.getLong(key, -1) ?: def
fun Fragment.boolExtra(key: String, def: Boolean = false) = arguments?.getBoolean(key, def) ?: def
inline fun DialogFragment.putPrams(crossinline func: Bundle.() -> Unit) = apply {
    arguments = Bundle().apply {
        func()
    }
}

fun Fragment.putPrams(func: Bundle.() -> Unit) = apply {
    arguments = Bundle().apply {
        func()
    }
}
