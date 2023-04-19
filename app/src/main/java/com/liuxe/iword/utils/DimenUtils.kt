package com.liuxe.iword.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.view.View

/**
 *  author : liuxe
 *  date : 4/27/21 2:09 PM
 *  description : dp、px相互转换
 */

object DimenUtils {

    fun dp2px(context: Context, dp:Float):Float=dp * context.resources.displayMetrics.density

    fun px2dp(context: Context, px:Float):Float =px / context.resources.displayMetrics.density


    /**
     * 获取状态栏高度
     *
     * @param context 目标Context
     */
    fun getStatusBarHeight(context: Context): Int {
        // 获得状态栏高度
        val resourceId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 获取屏幕宽高信息
     *
     * @return
     */
    val screenMetrics: Point
        get() {
            val dm = Resources.getSystem().displayMetrics
            return Point(dm.widthPixels, dm.heightPixels)
        }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    val screenWidth: Int
        get() = Resources.getSystem().displayMetrics.widthPixels

    /**
     * 获取导航栏高度
     */
    val navigationBarHeight: Int
        get() {
            var result = 0
            val resourceId = Resources.getSystem()
                .getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = Resources.getSystem().getDimensionPixelSize(resourceId)
            }
            return result
        }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    val screenHeight: Int
        get() = Resources.getSystem().displayMetrics.heightPixels


    /**
     * 获取View的宽高
     *
     * @param view
     * @return
     */
    fun getViewSize(view: View): IntArray {
        val size = IntArray(2)
        val width = View.MeasureSpec.makeMeasureSpec(
            0,
            View.MeasureSpec.UNSPECIFIED
        )
        val height = View.MeasureSpec.makeMeasureSpec(
            0,
            View.MeasureSpec.UNSPECIFIED
        )
        view.measure(width, height)
        size[0] = view.measuredWidth
        size[1] = view.measuredHeight
        return size
    }
}