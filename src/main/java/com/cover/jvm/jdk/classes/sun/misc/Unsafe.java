package com.cover.jvm.jdk.classes.sun.misc;

public class Unsafe {
    
    public static native long allocateMemory(long bytes);
    
    public static native long allocateObject();
    
    public static native void initMemoryModel();
}
