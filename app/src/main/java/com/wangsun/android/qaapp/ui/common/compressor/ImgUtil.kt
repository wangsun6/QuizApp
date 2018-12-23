package com.wangsun.android.qaapp.ui.common.compressor

import android.graphics.*
import android.media.ExifInterface
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


object ImgUtil{

    fun compressImage(maxWidth: Int, maxHeight: Int, quality: Int, compressFormat: Bitmap.CompressFormat,
                      inputImage: File,destinationDirectoryPath: String):File {
        var scaledBitmap: Bitmap? = null
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var bmp: Bitmap? = BitmapFactory.decodeFile(inputImage.absolutePath, options)

        var actualHeight: Int = options.outHeight
        var actualWidth: Int = options.outWidth

        println ( "MaxW:$maxWidth \n MaxH:$maxHeight \n Quality:$quality \n UtilFile:${inputImage.absolutePath} " +
                "\n DPath:$destinationDirectoryPath \n Height:$actualHeight \n Weight:$actualWidth" )


        var imgRatio: Float = actualWidth.toFloat() / actualHeight.toFloat()
        val maxRatio: Float = (maxWidth / maxHeight).toFloat()

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = (maxHeight.toFloat() / actualHeight.toFloat())
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight
            } else if (imgRatio > maxRatio) {
                imgRatio = (maxWidth.toFloat() / actualWidth.toFloat())
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth
            } else {
                actualHeight = maxHeight
                actualWidth = maxWidth
            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
        options.inJustDecodeBounds = false
        //options.inDither = false
        //options.inPurgeable = true
        //options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        try {
            bmp = BitmapFactory.decodeFile(inputImage.absolutePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        try {

            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
//
        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap)
        canvas.matrix = scaleMatrix
        canvas.drawBitmap(bmp!!, middleX - bmp.width / 2, middleY - bmp.height / 2,
                Paint(Paint.FILTER_BITMAP_FLAG))
        bmp.recycle()
        val exif: ExifInterface
        try {
            exif = ExifInterface(inputImage.absolutePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
            } else if (orientation == 3) {
                matrix.postRotate(180f)
            } else if (orientation == 8) {
                matrix.postRotate(270f)
            }

            scaledBitmap = scaledBitmap?.let {
                Bitmap.createBitmap(scaledBitmap, 0, 0, it.width, it.height,
                        matrix, true)
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }

        var out: FileOutputStream? = null

        try {
            out = FileOutputStream(destinationDirectoryPath)
            //write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(compressFormat, quality, out)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return File(destinationDirectoryPath)
    }


    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
        return inSampleSize
    }
}