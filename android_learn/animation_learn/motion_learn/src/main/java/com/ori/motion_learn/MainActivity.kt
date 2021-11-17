package com.ori.motion_learn

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var isRes = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val simpleDateFormat = SimpleDateFormat("EEEE", Locale.CHINA)
        val format = simpleDateFormat.format(System.currentTimeMillis())
        btn.text=format

    }

}

inline val Float.dp2px: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

inline val Int.dp2px: Int
    get() = (this.toFloat().dp2px).toInt()