package com.cover.jvm;

import com.cover.jvm.hotspot.src.share.vm.oops.InstanceKlass;

public class Main {
    public static void main(String[] args) {
        javaMain();
    }
    
    public static void javaMain() {
        // 通过AppClassLoader加载main函数所在的类
        InstanceKlass mainKlass = BootClassLoader.loadMainKlass();
    }
}