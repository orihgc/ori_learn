package com.ori.image_learn

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val loadBitmap = loadBitmap()
        image.post {
           image.setImageBitmap(loadBitmap)
        }

    }

    private fun blurBitmap(context: Context?, bitmap: Bitmap): Bitmap? {
        val outBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val rs = RenderScript.create(context)
        val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        val allIn = Allocation.createFromBitmap(rs, bitmap)
        val allOut = Allocation.createFromBitmap(rs, outBitmap)
        blurScript.setRadius(15f)
        blurScript.setInput(allIn)
        blurScript.forEach(allOut)
        allOut.copyTo(outBitmap)
        rs.destroy()
        return outBitmap
    }

    private fun loadBitmap(): Bitmap? {

        try {
            val url =
                URL("https://p3.daliapp.net/img/h-static/upload/litbot/b3d5de30-b3bd-11ea-a434-d78f60365c81.jpg~noop.image")
            val openConnection = url.openConnection()
            openConnection.doInput = true
            openConnection.connect()
            val inputStream = openConnection.getInputStream()
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }
}