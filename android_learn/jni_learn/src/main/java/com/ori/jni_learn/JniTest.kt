package com.ori.jni_learn

class JniTest {

    companion object {
        init {
            System.loadLibrary("jni_learn")
        }

        external fun stringFromJNI(): String

    }

    external fun stringFromJNI(): String


}