#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_ori_jni_1learn_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_ori_jni_1learn_JniTest_00024Companion_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++ impl Java_com_ori_jni_1learn_JniTest_stringFromJNI";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_ori_jni_1learn_JniTest_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++ impl Java_com_ori_jni_1learn_JniTest_stringFromJNI";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_ori_jni_1learn_JavaJniTest_stringFromJNI(
        JNIEnv* env,
        jclass clazz) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}