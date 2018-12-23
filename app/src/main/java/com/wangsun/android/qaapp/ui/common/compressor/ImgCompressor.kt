package com.wangsun.android.qaapp.ui.common.compressor

import android.content.Context
import android.graphics.Bitmap
import io.reactivex.Flowable
import java.io.File
import java.io.IOException
import java.util.concurrent.Callable


class ImgCompressor(context: Context){
    private var name: String = ""
    private var maxWidth = 1280
    private var maxHeight = 1280
    private var compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    private var quality = 85
    private var destinationDirectoryPath: String = context.getCacheDir().getPath() + File.separator + "images"


    fun setMaxWidth(maxWidth: Int): ImgCompressor {
        this.maxWidth = maxWidth
        return this
    }

    fun setMaxHeight(maxHeight: Int): ImgCompressor {
        this.maxHeight = maxHeight
        return this
    }

    fun setCompressFormat(compressFormat: Bitmap.CompressFormat): ImgCompressor {
        this.compressFormat = compressFormat
        return this
    }

    fun setQuality(quality: Int): ImgCompressor {
        this.quality = quality
        return this
    }

    fun setName(name: String): ImgCompressor {
        this.name = name
        return this
    }

    fun setDestinationDirectoryPath(destinationDirectoryPath: String): ImgCompressor {
        this.destinationDirectoryPath = destinationDirectoryPath
        return this
    }


    fun compressToFileAsFlowable(imageFile: File): Flowable<File> {
        if(name.equals(""))
            return compressToFileAsFlowable(imageFile, imageFile.name)
        else
            return compressToFileAsFlowable(imageFile, name)
    }

    private fun compressToFileAsFlowable(imageFile: File, compressedFileName: String): Flowable<File> {

        return Flowable.defer(Callable<Flowable<File>> {
            try {
                return@Callable Flowable.just(compressToFile(imageFile, compressedFileName))
            } catch (e: IOException) {
                return@Callable Flowable.error(e)
            }
        })
    }

    @Throws(IOException::class)
    fun compressToFile(inputImage: File,compressedFileName: String): File{
        return ImgUtil.compressImage(maxWidth,maxHeight,quality,compressFormat,
            inputImage,destinationDirectoryPath+ File.separator + compressedFileName)
    }

}