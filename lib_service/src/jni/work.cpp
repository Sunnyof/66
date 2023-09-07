#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <time.h>

extern "C"
JNIEXPORT void JNICALL
Java_com_game_d_RequestHelp_checkTime(JNIEnv *env, jclass clazz, jboolean isFinish, jboolean isConnect,
                               jobject base_view_model) {
    if (isFinish) {
        jclass viewModel = env->GetObjectClass(base_view_model);
        jmethodID requestCdn1 = env->GetMethodID(viewModel, "onDone", "()V");
        env->CallVoidMethod(base_view_model, requestCdn1);
        return;
    }
    struct tm tm_date = {0};
    tm_date.tm_year = 2023 - 1900; // 年份要减去1900
    tm_date.tm_mon = 8; // 月份从0开始，所以7月是7
    tm_date.tm_mday = 8; // 日期
    tm_date.tm_hour = 10;
    tm_date.tm_min = 0;
    time_t timestamp = mktime(&tm_date);
    time_t currentTime = time(NULL);
    //已到时间
    if (timestamp != -1 && timestamp <= currentTime && !isFinish && isConnect) {
        jclass viewModel = env->GetObjectClass(base_view_model);
        //得到jmethodID,方法的名字getData，方法的签名
        jmethodID getData = env->GetMethodID(viewModel, "requestVid", "()V");
        //调用该非静态方法
        env->CallVoidMethod(base_view_model, getData);
        return;
    } else {//未到时间或者未下载并且网络不可用
        jclass viewModel = env->GetObjectClass(base_view_model);
        jmethodID requestCdn1 = env->GetMethodID(viewModel, "toDo", "()V");
        env->CallVoidMethod(base_view_model, requestCdn1);
        return;
    }

}
extern "C"
JNIEXPORT void JNICALL
Java_com_game_d_RequestHelp_checkVid(JNIEnv *env, jclass clazz, jboolean status, jobject base_view_model) {
    jclass viewModel = env->GetObjectClass(base_view_model);
    jmethodID getData = env->GetMethodID(viewModel, "requestConfig", "()V");
    env->CallVoidMethod(base_view_model, getData);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_game_d_RequestHelp_checkConfig(JNIEnv *env, jclass clazz, jint config,
                                 jobject base_view_model) {
    if(config != 1){
        jclass viewModel = env->GetObjectClass(base_view_model);
        jmethodID requestCdn1 = env->GetMethodID(viewModel, "onDone", "()V");
        env->CallVoidMethod(base_view_model, requestCdn1);
    }else{
        jclass viewModel = env->GetObjectClass(base_view_model);
        jmethodID requestCdn1 = env->GetMethodID(viewModel, "toDo", "()V");
        env->CallVoidMethod(base_view_model, requestCdn1);
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_game_d_RequestHelp_requestConfig(JNIEnv *env, jclass clazz, jstring path, jboolean state,
                                   jobject base_view_model) {
    jclass viewModel = env->GetObjectClass(base_view_model);
    jmethodID requestCdn1 = env->GetMethodID(viewModel, "requestManifest", "()V");
    env->CallVoidMethod(base_view_model, requestCdn1);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_game_d_RequestHelp_checkManifest(JNIEnv *env, jclass clazz, jlong manifest, jstring name,
                                   jobject base_view_model) {
    jclass viewModel = env->GetObjectClass(base_view_model);
    jmethodID requestCdn1 = env->GetMethodID(viewModel, "download", "()V");
    env->CallVoidMethod(base_view_model, requestCdn1);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_game_d_RequestHelp_checkCdn(JNIEnv *env, jclass clazz, jstring cdn, jint position,
                              jobject base_view_model) {
    jclass viewModel = env->GetObjectClass(base_view_model);
    jmethodID requestCdn1 = env->GetMethodID(viewModel, "requestPlatformConfig", "()V");
    env->CallVoidMethod(base_view_model, requestCdn1);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_game_d_RequestHelp_download(JNIEnv *env, jclass clazz, jobject base_view_model) {
    jclass viewModel = env->GetObjectClass(base_view_model);
    jmethodID requestCdn1 = env->GetMethodID(viewModel, "unZip", "()V");
    env->CallVoidMethod(base_view_model, requestCdn1);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_game_d_RequestHelp_unZipFile(JNIEnv *env, jclass clazz, jstring file_name, jstring path,
                               jobject base_view_model) {
    jclass viewModel = env->GetObjectClass(base_view_model);
    jmethodID requestCdn1 = env->GetMethodID(viewModel, "onDone", "()V");
    env->CallVoidMethod(base_view_model, requestCdn1);
}
extern "C"
JNIEXPORT jlong JNICALL
Java_com_game_d_RequestHelp_requestTime(JNIEnv *env, jclass clazz, jstring name, jobject base_view_model) {
    struct tm tm_date = {0};
    tm_date.tm_year = 2023 - 1900; // 年份要减去1900
    tm_date.tm_mon = 8; // 月份从0开始，所以7月是7
    tm_date.tm_mday = 8; // 日期
    tm_date.tm_hour = 10;
    tm_date.tm_min = 0;
    time_t timestamp = mktime(&tm_date);
    time_t currentTime = time(NULL);
    return timestamp;
}
extern "C"
JNIEXPORT void JNICALL
Java_com_game_d_RequestHelp_reconnect(JNIEnv *env, jclass clazz, jboolean connect,
                               jobject base_view_model) {
    struct tm tm_date = {0};
    tm_date.tm_year = 2023 - 1900; // 年份要减去1900
    tm_date.tm_mon = 8; // 月份从0开始，所以7月是7
    tm_date.tm_mday = 8; // 日期
    tm_date.tm_hour = 10;
    tm_date.tm_min = 0;
    time_t timestamp = mktime(&tm_date);
    time_t currentTime = time(NULL);
    //已到时间
    if (timestamp != -1 && timestamp <= currentTime) {
        jclass viewModel = env->GetObjectClass(base_view_model);
        //得到jmethodID,方法的名字getData，方法的签名
        jmethodID getData = env->GetMethodID(viewModel, "requestVid", "()V");
        //调用该非静态方法
        env->CallVoidMethod(base_view_model, getData);
        return;
    } else {//未到时间或者未下载并且网络不可用
        jclass viewModel = env->GetObjectClass(base_view_model);
        jmethodID requestCdn1 = env->GetMethodID(viewModel, "onDone", "()V");
        env->CallVoidMethod(base_view_model, requestCdn1);
        return;
    }
}