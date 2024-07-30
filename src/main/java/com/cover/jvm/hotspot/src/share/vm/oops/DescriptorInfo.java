package com.cover.jvm.hotspot.src.share.vm.oops;

public class DescriptorInfo {

    /**
     * 是否完成解析并赋值
     * 默认false
     */
    private boolean isResolved = false;
    
    // 类型
    private int type;
    
    // 数组维度
    private int arrayDimension;
    
    // 类型
    private String typeDesc;
    
    private void incArrayDimentsion() {
        arrayDimension++;
    }
    
    public DescriptorInfo() {
        
    }
    
    public DescriptorInfo(boolean isResolved, int type) {
        this.isResolved = isResolved;
        this.type = type;
    }
    
    public DescriptorInfo(boolean isResolved, int type, String typeDesc) {
        this.isResolved = isResolved;
        this.type = type;
        this.typeDesc = typeDesc;
    }
    
    
}
