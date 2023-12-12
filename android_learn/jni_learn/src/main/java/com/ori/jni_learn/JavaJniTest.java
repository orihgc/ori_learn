package com.ori.jni_learn;

public class JavaJniTest {

    static {
        System.loadLibrary("jni_learn");
    }

    public static native String stringFromJNI();

}
