package com.cover.jvm.hotspot.src.share.vm.oops;

import lombok.Data;

@Data
public class AttributeInfo {
    
    private int attrNameIndex;
    
    private int attrLength;
    
    // 用于存储klass的attribute
    private byte[] container;
    
    public void initContainer() {
        container = new byte[attrLength];
    }
}
