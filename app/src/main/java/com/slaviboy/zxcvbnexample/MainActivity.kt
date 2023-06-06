package com.slaviboy.zxcvbnexample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.slaviboy.zxcvbn.Zxcvbn

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val zxcvbn = Zxcvbn(resources)
        Log.i("info", "score: ${zxcvbn.measure("p@ssword").score}")
        Log.i("info", "score: ${zxcvbn.measure("hello what is your name").score}")
        Log.i("info", "score: ${zxcvbn.measure("gggggg4535").feedback?.getWarningText(resources)}")
    }
}