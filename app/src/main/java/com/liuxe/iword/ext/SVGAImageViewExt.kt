package com.liuxe.iword.ext

import android.graphics.*
import android.text.TextPaint
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.liuxe.iword.utils.LogUtils
import com.opensource.svgaplayer.*
import java.net.URL


fun SVGAImageView.loadAssetsSVGA(fileName: String, times: Int = 1, func: () -> Unit = {}) {
    showSVGAFile(fileName, times, complete = { func() })
}

inline fun SVGAImageView.showLinkSVGA(
    link: String, times: Int = 0,
    crossinline complete: (videoItem: SVGAVideoEntity) -> Unit = {},
    crossinline error: () -> Unit = {}

) {
    val callback = object : SVGAParser.ParseCompletion {
        override fun onComplete(videoItem: SVGAVideoEntity) {

            complete(videoItem)
        }

        override fun onError() {
            error()
        }
    }

    loops = times
    isVisible = true

    try {
        SVGAParser(this.context).decodeFromURL(URL(link), callback)
    } catch (t: Throwable) {
        println("TAG_Debug -> showLinkSVGA error $link")
    }
}

inline fun SVGAImageView.showComplete(
    link: String? = null, fileName: String? = null,
    crossinline func: () -> Unit = {}, times: Int = 0
) {

//    clearsAfterStop = true

    fileName?.let { f ->
        showSVGAFile(f, times, complete = { func() })
    }

    link?.let { l ->
        showLinkSVGA(l, times, complete = { videoEntity ->
            setVideoItem(videoEntity)
            startAnimation()
            func()
        })
    }
}

inline fun SVGAImageView.showSVGAFile(
    fileName: String, times: Int = 1,
    crossinline complete: (videoItem: SVGAVideoEntity) -> Unit = {},
    crossinline error: () -> Unit = {}

): SVGAParser.ParseCompletion {
    val callback = object : SVGAParser.ParseCompletion {
        override fun onComplete(videoItem: SVGAVideoEntity) {
            setImageDrawable(SVGADrawable(videoItem))
            startAnimation()
            complete(videoItem)
        }

        override fun onError() {
            LogUtils.e("showSVGAFile  onError ")
            error()
        }
    }
    try {
        loops = times
        SVGAParser(context).decodeFromAssets("$fileName.svga", callback)
    } catch (t: Throwable) {

    }
    return callback
}


fun SVGAImageView.loadLinkSVGA(link: String? = null, times: Int = 1) {

    if (link.isNullOrBlank()) {
        return
    }

    try {
        loops = times
        SVGAParser(context).decodeFromURL(URL(link), object : SVGAParser.ParseCompletion {
            override fun onComplete(videoItem: SVGAVideoEntity) {
                setImageDrawable(SVGADrawable(videoItem))
                startAnimation()
            }

            override fun onError() {
                LogUtils.e("loadLinkSVGA  onError ")
            }
        })
    } catch (t: Throwable) {
    }
}



fun SVGAImageView.showAssetsRide(
    fileName: String,
    times: Int = 1,
    avatar: Int? = null,
    userName: String? = null,
    sp: Int = 10
) {

    if (isAnimating) {
        return
    }

    avatar?.let {

        showSVGAFile(fileName, times, complete = { videoEntity ->
            var bitmap= BitmapFactory.decodeResource(context.resources, avatar)

            var bm = toRoundBitmap(bitmap)?:bitmap
            val dynamicEntity = SVGADynamicEntity()
            //设置头像
            dynamicEntity.setDynamicImage(bm, "key_ride_avatar")
            //设置内容
            val objPaint = TextPaint()
            objPaint.color = Color.parseColor("#ffffff")
            objPaint.textSize = sp.sp()
            dynamicEntity.setDynamicText("$userName 进入自习室                    ", objPaint, "key_ride_banner")

            setImageDrawable(SVGADrawable(videoEntity, dynamicEntity))
            startAnimation()
        })

    } ?: showSVGAFile(fileName, times)


}


fun toRoundBitmap(bitmap: Bitmap?): Bitmap? {
    // 前面同上，绘制图像分别需要bitmap，canvas，paint对象
    var bitmap = bitmap
    bitmap = Bitmap.createScaledBitmap(bitmap!!, 400, 400, true)
    val bm = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bm)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    // 这里需要先画出一个圆
    canvas.drawCircle(200f, 200f, 200f, paint)
    // 圆画好之后将画笔重置一下
    paint.reset()
    // 设置图像合成模式，该模式为只在源图像和目标图像相交的地方绘制源图像
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(bitmap, 0f, 0f, paint)
    return bm
}