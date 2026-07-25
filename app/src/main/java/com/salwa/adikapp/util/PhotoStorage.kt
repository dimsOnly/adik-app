package com.salwa.adikapp.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/**
 * Semua foto note harian disimpan di internal storage aplikasi
 * (files/diary_photos), sehingga aplikasi tetap berjalan 100% offline
 * tanpa perlu upload ke server mana pun.
 */
object PhotoStorage {

    private fun diaryPhotoDir(context: Context): File {
        val dir = File(context.filesDir, "diary_photos")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun createNewPhotoFile(context: Context): File {
        val fileName = "diary_${UUID.randomUUID()}.jpg"
        return File(diaryPhotoDir(context), fileName)
    }

    fun uriForFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    fun saveBitmap(context: Context, bitmap: Bitmap): String {
        val file = createNewPhotoFile(context)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
        }
        return file.absolutePath
    }

    fun deletePhoto(path: String) {
        val file = File(path)
        if (file.exists()) file.delete()
    }
}
