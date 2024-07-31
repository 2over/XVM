package com.cover.jvm.hotspot.src.share.vm.runtime;

import com.cover.jvm.hotspot.src.share.vm.oops.MethodInfo;
import lombok.Data;

@Data
public class JavaVFrame {
    
    private StackValueCollection locals;
    
    private StackValueCollection stack = new StackValueCollection();
    
    private MethodInfo ownerMethod;
    
    public JavaVFrame(int maxLocals) {
        locals = new StackValueCollection(maxLocals);
    }
    
    public JavaVFrame(int maxLocals, MethodInfo methodInfo) {
        locals = new StackValueCollection(maxLocals);
        ownerMethod = methodInfo;
    }
}
