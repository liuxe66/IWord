package com.liuxe.iword.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.os.IBinder
import android.util.TypedValue
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair


fun Int.dp() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics)
fun Int.sp() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics)


/**
 * 取得虚拟导航栏高度
 */
fun Activity.getNavigationBarHeight(): Int {
    val display: Display = this.windowManager.defaultDisplay
    val size = Point()
    val realSize = Point()
    display.getSize(size)
    display.getRealSize(realSize)
    val resourceId: Int = Resources.getSystem().getIdentifier("navigation_bar_height", "dimen", "android")
    val height: Int = Resources.getSystem().getDimensionPixelSize(resourceId)
    //超出系统默认的导航栏高度以上，则认为存在虚拟导航
    return if (realSize.y - size.y > height - 10) {
        height
    } else 0
}

/**
 * 隐藏输入法
 */
fun Activity.hideKeyboard(token: IBinder?): Boolean {
    if (token != null) {
        val im: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
    }
    return false
}

/**
 * 下面两个跳转activity的方法，一个是结束自己一个不结束自己
 * ，省略掉this参数，kotlin写this太长了，而且有时候编译器还不提示的。。。
 *
 * @param block 在这里携带intent参数
 */
inline fun <reified T> Activity.launchActivity(block: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply { block() })
}

inline fun <reified T> Activity.toActivity(block: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply { block() })
    finish()
}

fun Activity.launchActivity(action: String, block: Intent.() -> Unit = {}) {
    startActivity(Intent().apply {
        setAction(action)
        block()
    })
}

inline fun <reified T> Activity.launchActivityForResult(requestCode: Int, block: Intent.() -> Unit = {}) {
    startActivityForResult(Intent(this, T::class.java).apply {
        block()
    }, requestCode)
}

/**
 * 带着过度动画启动
 */
inline fun <reified T : Activity> Activity.launchActivity(vararg sharedElements: Pair<View, String>, block: Intent.() -> Unit = {}) {
    ActivityCompat.startActivity(
            this,
            Intent(this, T::class.java).apply { block() },
            ActivityOptionsCompat.makeSceneTransitionAnimation(this, *sharedElements).toBundle()
    )
}

/**
 * 是否存在刘海屏
 */
fun Activity.isCutoutDisplay(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val windowInsets = window?.decorView?.rootWindowInsets?.displayCutout
        windowInsets != null
    } else
        false
}

/**
 * 取得刘海高度
 */
fun Activity.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android")

    if (resourceId > 0) {
        result = Resources.getSystem().getDimensionPixelSize(resourceId)
    }
    return result
}

fun Activity.setStatusBarColor(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = color
    }
}

fun Activity.stringExtra(key: String, def: String = "") = intent.getStringExtra(key) ?: def
fun Activity.intExtra(key: String, def: Int = -1) = intent.getIntExtra(key, def)
fun Activity.longExtra(key: String, def: Long = -1L) = intent.getLongExtra(key, def)
fun Activity.boolExtra(key: String, def: Boolean = false) = intent.getBooleanExtra(key, def)

/**
 * 标题栏黑色（暗色？）字体，前提是要设置全屏
 */
fun Activity.lightStatusBarTheme() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

/**
 * 标题栏黑色（暗色？）字体，且状态栏底色为纯白色
 */
fun Activity.lightStatusBarTheme2() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)
    }
}

/**
 * 标题栏白色字体，前提是要设置全屏
 */
fun Activity.darkStatusBarTheme() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }
}
