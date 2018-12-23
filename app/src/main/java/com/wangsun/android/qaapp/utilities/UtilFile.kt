package com.wangsun.android.qaapp.utilities

import android.os.Environment
import java.io.File

object UtilFile {
    fun getProfileImagePath(): String{
        val file_1 = File("${Environment.getExternalStorageDirectory()}/.qaapp/ProfileImages")
        if(!file_1.exists())
            file_1.mkdirs()
        return file_1.getAbsolutePath()
    }
}