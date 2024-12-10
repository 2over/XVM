package com.cover.jvm.jdk.classes;

public class JniEnv {
    
    public native static Handle getMethod(Handle kalss, String name, String descriptorName);
    
    public native static void CallStaticVoidMethod(Handle klass, Handle method);
}
