package com.ori.choice_set.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ori.choice_set.R

/**
 * Created by huangguocheng on 2024/1/18
 * @author huangguocheng@bytedance.com
 */
class NovaCheckBox @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attributeSet, defStyleAttr) {


    private var checkBox: CheckBox?
    private var cornerImageView: ImageView?
    private var container: ConstraintLayout?

    init {
        val novaCheckBox = LayoutInflater.from(context).inflate(R.layout.nova_check_box, this)
        container = novaCheckBox.findViewById(R.id.nova_checkbox)
        checkBox = novaCheckBox.findViewById(R.id.checkbox)
        cornerImageView = novaCheckBox.findViewById(R.id.iv_corner_icon)
        checkBox?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cornerImageView?.visibility = View.VISIBLE
                checkBox?.setTextColor(context.resources.getColor(R.color.checkbox_checked_text_color))
            } else {
                cornerImageView?.visibility = View.GONE
                checkBox?.setTextColor(Color.BLACK)
            }
        }

    }
}