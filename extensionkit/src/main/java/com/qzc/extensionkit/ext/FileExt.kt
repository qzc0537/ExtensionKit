package com.qzc.extensionkit.ext

import android.content.ContentUris
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.core.content.FileProvider
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.qzc.extensionkit.utils.getMimeType
import java.io.*
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
fun Context.deleteFile(path: String): Boolean {
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


fun Context.isAndroidQFileExists(path: String): Boolean {
    var afd: AssetFileDescriptor? = null
    val cr = this.contentResolver
    try {
        val uri = Uri.parse(path)
        afd = cr.openAssetFileDescriptor(uri, "r")
        afd?.let {
            afd.close()
        } ?: return false
    } catch (e: FileNotFoundException) {
        return false
    } finally {
        afd?.close()
    }
    return true
}

/**
 * 把内容写入文件
 *
 * @param content
 */
fun saveTxtToSD(context: Context, content: String) {
    //生成文件夹之后，再生成文件，不然会出错
    val fileName = "log.txt"

    val parentFile = File(context.externalCacheDir.absolutePath + "/" + "Log")
    if (!parentFile.exists()) {
        parentFile.mkdir()
    }
    val file = File(parentFile, fileName)
    if (!file.exists()) {
        try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    var out: BufferedWriter? = null
    try {
        out = BufferedWriter(
            OutputStreamWriter(
                FileOutputStream(file, true), "gb2312"
            )
        )
        out.write(content)
        out.write("\r\n")
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            out!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

/**
 * Get a file path from a Uri. This will get the the path for Storage Access
 * Framework Documents, as well as the _data field for the MediaStore and
 * other file-based ContentProviders.<br></br>
 * <br></br>
 * Callers should check whether the path is local before assuming it
 * represents a local file.
 *
 * @param context The context.
 * @param uri     The Uri to query.
 */
@RequiresApi(Build.VERSION_CODES.KITKAT)
fun getPath(context: Context?, uri: Uri?): String? {
    var path: String? = null
    if (null == context || uri == null)
        return null

    val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    // DocumentUri
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        // ExternalStorageDocumentsUri
        if (isExternalStorageDocumentsUri(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val splits =
                docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = splits[0]
            if ("primary".equals(type, ignoreCase = true)) {
                path =
                    Environment.getExternalStorageDirectory().toString() + File.separator + splits[1]
            }
        } else if (isDownloadsDocumentsUri(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                java.lang.Long.valueOf(docId)
            )
            path = getDataColumn(context, contentUri, null, null)
        } else if (isMediaDocumentsUri(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])
            path = getDataColumn(context, contentUri, selection, selectionArgs)
        }// MediaDocumentsUri
        // DownloadsDocumentsUri
    } else if ("content".equals(uri.scheme, true)) {
        if (isGooglePhotosContentUri(uri))
            return uri.lastPathSegment
        path = getDataColumn(context, uri, null, null)
    } else if ("file".equals(uri.scheme, true)) {
        path = uri.path
    }// File
    // MediaStore (general)
    return path ?: getFilePathByCopyFile(context, uri)
}

/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.
 *
 * @param context       The context.
 * @param uri           The Uri to query.
 * @param selection     (Optional) Filter used in the query.
 * @param selectionArgs (Optional) Selection arguments used in the query.
 * @return The value of the _data column, which is typically a file path.
 * @author paulburke
 */
fun getDataColumn(
    context: Context, uri: Uri?, selection: String?,
    selectionArgs: Array<String>?
): String? {

    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)

    try {
        cursor =
            context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val column_index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(column_index)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        cursor?.close()
    }
    return null
}

fun isExternalStorageDocumentsUri(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

fun isDownloadsDocumentsUri(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

fun isMediaDocumentsUri(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

fun isGooglePhotosContentUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri.authority
}

fun getFilePathByCopyFile(context: Context, contentUri: Uri): String? {
    val rootDataDir = context.filesDir
    val fileName = getFileName(contentUri)
    if (!TextUtils.isEmpty(fileName)) {
        val copyFile = File(rootDataDir.toString() + File.separator + fileName)
        copyFile(context, contentUri, copyFile)
        return copyFile.absolutePath
    }
    return null
}

fun getFileName(uri: Uri?): String? {
    if (uri == null) return null
    var fileName: String? = null
    val path = uri.path ?: return fileName
    val count = path.lastIndexOf('/')
    if (count != -1) {
        fileName = path.substring(count + 1)
    }
    return fileName
}

fun copyFile(context: Context, srcUri: Uri, dstFile: File) {
    try {
        val inputStream = context.contentResolver.openInputStream(srcUri) ?: return
        val outputStream = FileOutputStream(dstFile)
        copyStream(inputStream, outputStream)
        inputStream.close()
        outputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

@Throws(Exception::class, IOException::class)
fun copyStream(input: InputStream, output: OutputStream): Int {
    val BUFFER_SIZE = 1024 * 2
    val buffer = ByteArray(BUFFER_SIZE)
    val inputStream = BufferedInputStream(input, BUFFER_SIZE)
    val outputStream = BufferedOutputStream(output, BUFFER_SIZE)
    var count = 0
    var readCount = -1
    try {
        do {
            readCount = inputStream.read(buffer, 0, BUFFER_SIZE)
            if (readCount != -1) {
                outputStream.write(buffer, 0, readCount)
                count += readCount
            } else {
                break
            }
        } while (true)
        outputStream.flush()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        try {
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
    return count
}