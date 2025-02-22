package com.mrinsaf.core.data.repository.local

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import okhttp3.ResponseBody
import org.beyka.tiffbitmapfactory.TiffBitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object FileStorage {

    fun saveResponseBodyToStorage(context: Context, fileName: String, responseBody: ResponseBody): Uri? {
        return try {
            val byteArray = responseBody.bytes()  // Преобразуем тело ответа в массив байтов
            saveFileToStorage(context, fileName, byteArray)  // Сохраняем файл
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun saveFileToStorage(context: Context, fileName: String, byteArray: ByteArray): Uri? {
        return try {
            val file = File(context.filesDir, fileName)  // Путь в internal storage

            FileOutputStream(file).use { outputStream ->
                outputStream.write(byteArray)  // Запись массива байтов в файл
            }

            Uri.fromFile(file)  // Возвращаем URI файла
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
