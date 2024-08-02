package com.cover.jvm.jdk.classes.sun.misc;

import com.cover.jvm.jdk.classes.Handle;

public class AppClassLoader {
    public native static Handle loadKlass(String name);
}
