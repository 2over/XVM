package com.cover.jvm.hotspot.src.share.vm.classfile;

import java.util.ArrayList;

public class DescriptorStream2 {
    
    private String descriptor;
    
    private DEscriptorInfo returnElement;
    
    private int methodParamsSize;
    
    private List<DescriptorInfo> parameters = new ArrayList<>();
    
    public DEscriptorStream2(String descriptor) {
        this.descriptor = descriptor;
    }
    
    
    public void parseMethod() {
        parseMethodParams();
        parseReturn();
    }
    
    public void parseField() {
        returnElement = new DEscriptorStream(descriptor).parse();
    }
    
    public DescriptorInfo parseReturn() {
        int paramEndIndex = descriptor.indexOf(")");
        String returnStr = descriptor.substring(paramEndIndex + 1, descriptor.length());
        returnElement = new DescriptorStream(returnStr).parse();
        return returnElement;
    }
    
}
