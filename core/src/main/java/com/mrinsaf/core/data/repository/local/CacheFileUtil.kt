package com.mrinsaf.core.data.repository.local

import android.content.Context
import android.net.Uri
import android.util.Log
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

object CacheFileUtil {

    fun saveFileToCache(context: Context, fileName: String, responseBody: ResponseBody): Uri? {
        return try {
            // Определяем путь в кэше
            val file = File(context.cacheDir, fileName)

            // Сохраняем файл
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                inputStream = responseBody.byteStream()
                outputStream = FileOutputStream(file)

                val buffer = ByteArray(4096)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                outputStream.flush()
                Log.d("CacheFileUtil", "Файл сохранён в кэш: ${file.absolutePath}")

                // Возвращаем URI
                Uri.fromFile(file)
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: Exception) {
            Log.e("CacheFileUtil", "Ошибка при сохранении в кэш: ${e.message}")
            null
        }
    }
}
