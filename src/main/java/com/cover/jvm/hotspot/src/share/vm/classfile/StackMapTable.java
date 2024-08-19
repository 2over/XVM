package com.cover.jvm.hotspot.src.share.vm.classfile;

import com.cover.jvm.hotspot.src.share.vm.oops.AttributeInfo;
import lombok.Data;

@Data
public class StackMapTable extends AttributeInfo {
    
    private int attrNameIndex;
    
    private int attrLength;
}
