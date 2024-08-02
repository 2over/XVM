package com.cover.jvm.hotspot.src.share.vm.classfile;

import cn.hutool.core.io.FileUtil;
import com.cover.jvm.hotspot.src.share.vm.oops.InstanceKlass;
import lombok.Data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Data
public class BootClassLoader {
    public static final String SUFFIX = ".class";

    /**
     * 该类加载器的加载路径
     * 多个路径以分号分割
     * 只有一个的话，分号可以省略
     */
    private static String searchPath = "";

    /**
     * 用于存储该类加载器加载的所有类
     */
    private static Map<String, InstanceKlass> classLoaderData = new HashMap<>();

    /**
     * main函数所在的类在此保存一份引用，方便快速定位到
     */
    private static InstanceKlass mainKlass = null;
    
    public static InstanceKlass getMainKlass() {
        return mainKlass;
    }
    
    public static void setMainKlass(InstanceKlass mainKlass) {
        BootClassLoader.mainKlass = mainKlass;
    }
    
    public static InstanceKlass loadKlass(String name) {
        return loadKlass(name, true);
    }
    
    public static InstanceKlass loadKlass(String name, Boolean resolve) {
        InstanceKlass klass = findLoadedKlass(name);
        if (null != klass) {
            return klass;
        }
        
        klass = readAndParse(name);
        if (resolve) {
            resolveKlass();
        }
        return klass;
    }
    
    private static InstanceKlass readAndParse(String name) {
        String tmpName = name.replace(".", "/");
        String filePath = searchPath + tmpName + SUFFIX;
        
        // 读取字节码文件
        byte[] content = FileUtil.readBytes(new File(filePath));
        
        // 解析字节码文件
        InstanceKlass klass = ClassFileParser.parseClassFile(null, content);
        // 存入
        classLoaderData.put(name, klass);
        return klass;
    }
    
    private static void resolveKlass() {
        
    }
    
    public static InstanceKlass findLoadedKlass(String name) {
        return classLoaderData.get(name);
    }
    
    public static InstanceKlass loadMainKlass(String name) {
        if (null != mainKlass) {
            return mainKlass;
        }
        
        return loadKlass(name);
    }
    
}
