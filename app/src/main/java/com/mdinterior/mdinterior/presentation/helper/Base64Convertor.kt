package com.mdinterior.mdinterior.presentation.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64


object Base64Convertor {
    fun convertToBitmap(imgString: String): Bitmap? {
        val imageBytes = Base64.decode(imgString, 0)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}