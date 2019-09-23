package com.qzc.extensionkit.ext

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import java.io.*
import android.graphics.PixelFormat
import android.graphics.drawable.NinePatchDrawable
import android.R.drawable
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable


/**
 * created by qzc at 2019/09/23 20:37
 * desc:
 */

fun Context.decodeResource(id: Int, options: BitmapFactory.Options? = null): Bitmap {
    return BitmapFactory.decodeResource(resources, id, options)
}

fun decodeFile(path: String, options: BitmapFactory.Options? = null): Bitmap {
    return BitmapFactory.decodeFile(path, options)
}

fun ByteArray.byteToBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}

fun Bitmap.bitmapToByte(): ByteArray {
    val bos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, bos)
    return bos.toByteArray()
}

fun Bitmap.bitmapToDrawable(context: Context): Drawable {
    return BitmapDrawable(context.resources, this)
}

fun Drawable.drawableToBitmap(): Bitmap? {
    when {
        this is BitmapDrawable -> return this.bitmap
        this is NinePatchDrawable -> {
            val bitmap = Bitmap.createBitmap(
                this.getIntrinsicWidth(),
                this.getIntrinsicHeight(),
                if (this.getOpacity() !== PixelFormat.OPAQUE)
                    Bitmap.Config.ARGB_8888
                else
                    Bitmap.Config.RGB_565
            )
            val canvas = Canvas(bitmap)
            this.setBounds(0, 0, this.getIntrinsicWidth(), this.getIntrinsicHeight())
            this.draw(canvas)
            return bitmap
        }
        else -> return null
    }
}

/**
 * 保存图片到应用存储中
 */
fun Context.saveToInternal(bitmap: Bitmap, file: File): Boolean {
    var fos: FileOutputStream? = null
    try {
        fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        return true
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            fos?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return false
}

/**
 * 保存图片到相册
 * @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
 */
fun Context.saveToGallery(bitmap: Bitmap, desFile: File): Boolean {
    var fos: FileOutputStream? = null
    try {
        fos = FileOutputStream(desFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        //通知系统相册刷新
        sendBroadcast(
            Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                fromFile(File(desFile.path))
            )
        )
        return true
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            fos?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return false
}

/**
 * 质量压缩
 * @param quality 图片的质量,0-100,数值越小质量越差
 * @param format  图片格式 jpeg,png,webp
 * 质量压缩并不会改变图片在内存中的大小，仅仅会减小图片所占用的磁盘空间的大小，
 * 因为质量压缩不会改变图片的分辨率，而图片在内存中的大小是根据width*height*一个像素的所占用的字节数计算的，
 * 宽高没变，在内存中占用的大小自然不会变
 */
fun Bitmap.compressQuality(
    quality: Int = 100,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
): Bitmap {
    val bos = ByteArrayOutputStream()
    this.compress(format, quality, bos)
    return BitmapFactory.decodeByteArray(bos.toByteArray(), 0, bos.size())
}

/**
 *采样率压缩
 * @param inSampleSize  可以根据需求计算出合理的inSampleSize
 * 1. inSampleSize小于等于1会按照1处理
 * 2. inSampleSize只能设置为2的平方，不是2的平方则最终会减小到最近的2的平方数，如设置7会按4进行压缩
 */
fun Context.compressSampledFromResource(resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {
    // First decode with inJustDecodeBounds=true to check dimensions
    return BitmapFactory.Options().run {
        inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, resId, this)

        // Calculate inSampleSize
        inSampleSize = calculateInSampleSize(
            this,
            reqWidth,
            reqHeight
        )

        // Decode bitmap with inSampleSize set
        inJustDecodeBounds = false

        BitmapFactory.decodeResource(resources, resId, this)
    }
}

/**
 *采样率压缩
 * @param inSampleSize  可以根据需求计算出合理的inSampleSize
 */
fun Context.compressSampledFromFile(path: String, reqWidth: Int, reqHeight: Int) {
    // First decode with inJustDecodeBounds=true to check dimensions
    return BitmapFactory.Options().run {
        inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, this)

        // Calculate inSampleSize
        inSampleSize = calculateInSampleSize(
            this,
            reqWidth,
            reqHeight
        )

        // Decode bitmap with inSampleSize set
        inJustDecodeBounds = false

        BitmapFactory.decodeFile(path, this)
    }
}

/**
 *采样率压缩
 * @param inSampleSize  可以根据需求计算出合理的inSampleSize
 */
fun ByteArray.compressSampledFromByte(reqWidth: Int, reqHeight: Int) {
    // First decode with inJustDecodeBounds=true to check dimensions
    val data = this
    return BitmapFactory.Options().run {
        inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(data, 0, data.size, this)

        // Calculate inSampleSize
        inSampleSize = calculateInSampleSize(
            this,
            reqWidth,
            reqHeight
        )

        // Decode bitmap with inSampleSize set
        inJustDecodeBounds = false

        BitmapFactory.decodeByteArray(data, 0, data.size, this)
    }
}

/**
 * 计算图片采样率
 */
fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {

        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}