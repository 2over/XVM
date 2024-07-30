package com.cover.jvm.hotspot.src.share.vm.oops;

import lombok.Data;

@Data
public class InterfaceInfo {
    private int constantPoolIndex;
    
    private String interfaceName;
    
    public InterfaceInfo(int index, String name) {
        this.constantPoolIndex = index;
        
        this.interfaceName = name;
    }
    
}
