package com.ori.ioc_learn

import android.view.View

class InjectManager {

    companion object {
        fun inject(any: Any) {
            injectLayout(any)
            injectView(any)
            injectClickAction(any)
        }

        private fun injectLayout(any: Any) {
            val clazz = any.javaClass
            val injectLayout = clazz.getAnnotation(InjectLayout::class.java)
            if (injectLayout != null) {
                try {
                    val method = clazz.getMethod("setContentView", Int::class.java)
                    method.invoke(any, injectLayout.value)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }

        private fun injectView(any: Any) {
            val clazz = any.javaClass
            clazz.declaredFields.forEach {
                val injectView = it.getAnnotation(InjectView::class.java)
                if (injectView != null) {
                    try {
                        val findViewById = clazz.getMethod("findViewById", Int::class.java)
                        val view = findViewById.invoke(any, injectView.value) as View
                        it.isAccessible = true
                        it.set(any, view)
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            }
        }

        private fun injectClickAction(any: Any) {
            val clazz = any.javaClass
            clazz.declaredMethods.forEach {
                val annotation = it.getAnnotation(OnClick::class.java)
                if (annotation != null) {
                    try {
                        val findViewById = clazz.getMethod("findViewById", Int::class.java)
                        val view = findViewById.invoke(any, annotation.value) as? View
                        view?.setOnClickListener { _ ->
                            it.invoke(any)
                        }
                    } catch (e: Throwable) {

                    }
                }
            }
        }
    }
}