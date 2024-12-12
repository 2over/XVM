package com.cover.jvm.jdk.classes;


/**
 * 生成java 头文件
 * 1. 进入到该目录 /home/ziya/IdeaProjects/XVM/src/main/java
 * 2.javac com/cover/jvm/jdk/classes/Handle.java
 * 3.javac -h  /home/ziya/IdeaProjects/XVM/jni  com/cover/jvm/jdk/classes/JniEnv.java
 *
 */
public class JniEnv {
    
    public native static Handle getMethod(Handle klass, String name, String descriptorName);
    
    public native static void CallStaticVoidMethod(Handle klass, Handle method);
}
