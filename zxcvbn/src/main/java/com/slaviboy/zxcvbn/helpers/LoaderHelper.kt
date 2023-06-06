package com.slaviboy.zxcvbn.helpers

import android.content.res.Resources
import java.io.IOException

object LoaderHelper {

    @Throws(IOException::class)
    fun load(resources: Resources, name: String, fileResId: Int): String {
        return try {
            resources
                .openRawResource(fileResId)
                .bufferedReader()
                .use {
                    it.readText()
                }
        } catch (e: IOException) {
            throw RuntimeException("Error while reading $name")
        }
    }
}