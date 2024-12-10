package com.cover.jvm.jdk.classes.sun.misc;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;

import java.io.File;

public class ClassLoader {
    
    public byte[] readFile(String filepath) {
        Assert.isNull(filepath);
        
        File file = new File(filepath);
        
        return FileUtil.readBytes(file);
    }
}
