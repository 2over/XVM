package com.cover.jvm.hotspot.src.share.vm.oops;

import lombok.Data;

@Data
public class FieldInfo {
    
    private int accessFlags;
    
    private int nameIndex;
    
    private int descriptorIndex;
    
    private int attributesCount;
    
    private CodeAttributeInfo[] attributes;
}
