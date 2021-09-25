package com.ori.motion_learn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.transition.TransitionManager
import androidx.core.view.isVisible
import androidx.transition.Fade
import androidx.transition.Slide
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {
            TransitionManager.beginDelayedTransition(card, CustomTransition())
            card.y = 0F
            card.alpha=0F
        }
    }
}