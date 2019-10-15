package com.qzc.extensionkit.ext

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.content.FileProvider
import android.text.TextUtils
import com.qzc.extensionkit.utils.getMimeType
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.text.DecimalFormat

/**
 * created by qzc at 2019/09/22 10:13
 * desc:
 */

/**
 * 判断sdcard是否已经挂载
 *
 * @return boolean true:挂载 false:未挂载
 */
fun Context.isSDCardMounted(): Boolean {
    return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
}

/**
 * 得到sdcard的根路径
 *
 * @return /storage/emulated/0
 */
fun Context.getSDCardDir(): File? {
    var root: File? = null
    if (isSDCardMounted()) {
        root = Environment.getExternalStorageDirectory()
    }
    return root
}

/**
 * 获取sdcard的图片缓存目录
 *
 * @return /storage/emulated/0/Pictures
 */
fun Context.getPictureDir(): File? {
    var cachePath: File? = null
    if (isSDCardMounted()) {
        cachePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    }
    return cachePath
}

/**
 * 删除文件
 */
fun Context.delFile(path: String): Boolean {
    if (TextUtils.isEmpty(path)) {
        return false
    }
    val file = File(path)
    if (file.isFile && file.exists()) {
        try {
            file.delete()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    return false
}

/**
 * 递归删除子文件
 */
fun Context.recursiveDeleteFile(path: String) {
    val file = File(path)
    if (file.exists()) {
        if (file.isDirectory) {
            val files = file.listFiles()
            for (subFile in files) {
                if (subFile.isDirectory)
                    recursiveDeleteFile(subFile.path)
                else
                    subFile.delete()
            }
        }
    }
}

/**
 * 递归删除文件和文件夹
 */
fun Context.recursiveDeleteDir(file: File) {
    if (file.isFile) {
        file.delete()
        return
    }
    if (file.isDirectory) {
        val childFile = file.listFiles()
        if (childFile == null || childFile.isEmpty()) {
            file.delete()
            return
        }
        for (f in childFile) {
            recursiveDeleteDir(f)
        }
        file.delete()
    }
}

val File.canListFiles: Boolean
    get() = canRead() and isDirectory

val File.totalSize: Long
    get() = if (isFile) length() else getFolderSize(this)

val File.formatSize: String
    get() = getFormatFileSize(totalSize)

val File.mimeType: String
    get() = getMimeType(extension, isDirectory)

fun File.listFiles(
    isRecursive: Boolean = false,
    filter: ((file: File) -> Boolean)? = null
): Array<out File> {
    val fileList = if (!isRecursive) listFiles() else getAllSubFile(this)
    var result: Array<File> = arrayOf()
    return if (filter == null) fileList
    else {
        for (file in fileList) {
            if (filter(file)) result = result.plus(file)
        }
        result
    }
}

fun File.rename(newName: String) =
    rename(File("$parent${File.separator}$newName"))

fun File.rename(newFile: File) =
    if (newFile.exists()) false else renameTo(newFile)

fun writeText(content: String, filePath: String, fileName: String): Boolean {
    val strFilePath = filePath + fileName
    // 每次写入时，都换行写
    val strContent = content + "\r\n"
    var raf: RandomAccessFile? = null
    try {
        val file = File(strFilePath)
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
        raf = RandomAccessFile(file, "rwd")
        raf.seek(file.length())
        raf.write(strContent.toByteArray())
        return true
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        raf?.close()
    }
    return false
}

fun File.writeText(append: Boolean = false, text: String, charset: Charset = Charsets.UTF_8) {
    if (append) appendText(text, charset) else writeText(text, charset)
}

fun File.writeBytes(append: Boolean = false, bytes: ByteArray) {
    if (append) appendBytes(bytes) else writeBytes(bytes)
}

fun File.moveTo(destFile: File, overwrite: Boolean = true, reserve: Boolean = true): Boolean {
    val dest = copyRecursively(destFile, overwrite)
    if (!reserve) deleteRecursively()
    return dest
}

fun getFolderSize(file: File): Long {
    var total = 0L
    for (subFile in file.listFiles()) {
        total += if (subFile.isFile) subFile.length()
        else getFolderSize(subFile)
    }
    return total
}

fun getFormatFileSize(size: Long, unit: Int = 1000): String {
    val formatter = DecimalFormat("####.00")
    return when {
        size < 0 -> "0 B"
        size < unit -> "$size B"
        size < unit * unit -> "${formatter.format(size.toDouble() / unit)} KB"
        size < unit * unit * unit -> "${formatter.format(size.toDouble() / unit / unit)} MB"
        else -> "${formatter.format(size.toDouble() / unit / unit / unit)} GB"
    }
}

fun getAllSubFile(folder: File): Array<File> {
    var fileList: Array<File> = arrayOf()
    if (!folder.canListFiles) return fileList
    for (subFile in folder.listFiles())
        fileList = if (subFile.isFile) fileList.plus(subFile)
        else fileList.plus(getAllSubFile(subFile))
    return fileList
}

fun copyFile(
    sourceFile: File,
    destFile: File,
    overwrite: Boolean,
    func: ((file: File, progress: Int) -> Unit)? = null
) {

    if (!sourceFile.exists()) return

    if (destFile.exists()) {
        val stillExists = if (!overwrite) true else !destFile.delete()

        if (stillExists) {
            return
        }
    }

    if (!destFile.exists()) destFile.createNewFile()

    val inputStream = FileInputStream(sourceFile)
    val outputStream = FileOutputStream(destFile)
    val iChannel = inputStream.channel
    val oChannel = outputStream.channel


    val totalSize = sourceFile.length()
    val buffer = ByteBuffer.allocate(1024)
    var hasRead = 0f
    var progress = -1
    while (true) {
        buffer.clear()
        val read = iChannel.read(buffer)
        if (read == -1)
            break
        buffer.limit(buffer.position())
        buffer.position(0)
        oChannel.write(buffer)
        hasRead += read

        func?.let {
            val newProgress = ((hasRead / totalSize) * 100).toInt()
            if (progress != newProgress) {
                progress = newProgress
                it(sourceFile, progress)
            }
        }
    }

    inputStream.close()
    outputStream.close()
}

fun copyFolder(
    sourceFolder: File,
    destFolder: File,
    overwrite: Boolean,
    func: ((file: File, i: Int) -> Unit)? = null
) {
    if (!sourceFolder.exists()) return

    if (!destFolder.exists()) {
        val result = destFolder.mkdirs()
        if (!result) return
    }

    for (subFile in sourceFolder.listFiles()) {
        if (subFile.isDirectory) {
            copyFolder(
                subFile,
                File("${destFolder.path}${File.separator}${subFile.name}"),
                overwrite,
                func
            )
        } else {
            copyFile(subFile, File(destFolder, subFile.name), overwrite, func)
        }
    }
}

fun Context.fromFile(file: File, authority: String = packageName): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(this, authority, file)
    } else {
        Uri.fromFile(file)
    }
}

fun File.makeDir(): Boolean {
    try {
        return if (!this.exists()) {
            this.mkdir()
            true
        } else {
            true
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

fun Context.makeDir(pathname: String): File? {
    val file = File(pathname)
    val res = file.makeDir()
    return if (res) file else null
}

fun Context.makeDir(parent: String, child: String): File? {
    val file = File(parent, child)
    val res = file.makeDir()
    return if (res) file else null
}