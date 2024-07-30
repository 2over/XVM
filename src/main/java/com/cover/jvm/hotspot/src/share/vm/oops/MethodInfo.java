package com.cover.jvm.hotspot.src.share.vm.oops;

import com.cover.jvm.hotspot.src.share.vm.classfile.DescriptorStream2;

public class MethodInfo {
    
    private InstanceKlass belongKlass;
    
    private AccessFlags accessFlags;
    
    private  int nameIndex;
    
    private int descriptorIndex;
    
    private int attributesCount;
    
    private CodeAttributeInfo[] attributes;
    
    private String methodName;
    
    private DescriptorStream2 descriptor;
    
    private void initAttributeContainer() {
        attributes = new CodeAttributeInfo[attributesCount];
    }

    @Override
    public String toString() {
        return "MethodInfo{ }";
    }
}
