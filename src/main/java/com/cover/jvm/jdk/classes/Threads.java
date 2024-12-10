package com.cover.jvm.jdk.classes;

import java.util.HashMap;

public class Threads {
    
    private static HashMap<String, Long> container = new HashMap<>();
    
    public native static void createVM();
    
    public native static void gc();
    
    public native static void fullGc();
}
