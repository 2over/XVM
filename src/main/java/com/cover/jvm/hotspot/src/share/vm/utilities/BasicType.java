package com.cover.jvm.hotspot.src.share.vm.utilities;

public class BasicType {
    
    // JVM 源码BasicType.java
    public static final int T_BOOLEAN = 4;
    
    public static final int T_CHAR = 5;
    
    public static final int T_FLOAT = 6;
    
    public static final int T_DOUBLE = 7;
    
    public static final int T_BYTE = 8;
    
    public static final int T_SHORT = 9;
    
    public static final int T_INT = 10;
    
    public static final int T_LONG = 11;
    
    public static final int T_OBJECT = 12;
    
    public static final int T_ARRAY = 13;
    
    public static final int T_VOID= 14;
    
    public static final int T_ADDRESS = 15;
    
    public static final int T_NARROWOOP = 16;
    
    public static final int T_METADATA = 17;
    
    public static final int T_NARROWKLASS = 18;
    
    public static final int T_CONFLICT = 19;
    
    public static final int T_ILLEGAL = 99;
    
    public static final int T_Throwable = 100;
    
    public static final int JVM_SIGNATURE_ARRAY = '[';
    
    public static final int JVM_SIGNATURE_BYTE = 'B';
    
    public static final int JVM_SIGNATURE_CHAR = 'C';
    
    public static final int JVM_SIGNATURE_CLASS = 'L';
    
    public static final int JVM_SIGNATURE_ENDCLASS = ';';
    
    public static final int JVM_SIGNATURE_ENUM = 'E';
    
    public static final int JVM_SIGNATURE_FLOAT = 'F';
    
    public static final int JVM_SIGNATURE_DOUBLE ='D';
    
    public static final int JVM_SIGNATURE_FUNC = '(';
    
    public static final int JVM_SIGNATURE_ENDFUNC = ')';
    
    public static final int JVM_SIGNATURE_INT = 'I';
    
    public static final int JVM_SIGNATURE_LONG = 'J';
    
    public static final int JVM_SIGNATURE_SHORT = 'S';
    
    public static final int JVM_SIGNATURE_VOID = 'V';
    
    public static final int JVM_SIGNATURE_BOOLEAN = 'Z';
    
    // classfile_constant.h
    public static final int JVM_ACC_PUBLIC = 0x0001;
    
    public static final int JVM_ACC_PRIVATE = 0x0002;
    
    public static final int JVM_ACC_PROTECTED = 0x0004;
    
    public static final int JVM_ACC_STATIC = 0x0008;
    
    public static final int JVM_ACC_FINAL = 0x0010;
    
    public static final int JVM_ACC_SYNCHRONIZED = 0x0020;
    
    public static final int JVM_ACC_SUPER = 0x0020;
    
    public static final int JVM_ACC_VOLATILE = 0x0040;
    
    public static final int JVM_ACC_BRIDGE = 0x0040;
    
    public static final int JVM_ACC_TRANSIENT = 0x0080;
    
    public static final int JVM_ACC_VARARGS = 0x0080;
    
    public static final int JVM_ACC_NATIVE = 0x00100;
    
    public static final int JVM_ACC_INTERFACE = 0x00200;
    
    public static final int JVM_ACC_ABSTRACT = 0x00400;
    
    public static final int JVM_ACC_STRICT = 0x00800;
    
    public static final int JVM_ACC_SYNTHETIC = 0x001000;
    
    public static final int JVM_ACC_ANNOTATION = 0x002000;
    
    public static final int JVM_ACC_ENUM = 0x004000;
    
}
