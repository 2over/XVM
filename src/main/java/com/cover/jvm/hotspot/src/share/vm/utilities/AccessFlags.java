package com.cover.jvm.hotspot.src.share.vm.utilities;

import lombok.Data;

@Data
public class AccessFlags {
    
    private int flag;
    
    public AccessFlags(int flag) {
        this.flag = flag;
    }
    
    public boolean isStatic() {
        return (flag & BasicType.JVM_ACC_STATIC) != 0;
    }
}
