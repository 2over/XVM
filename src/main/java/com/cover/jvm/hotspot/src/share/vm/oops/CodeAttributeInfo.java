package com.cover.jvm.hotspot.src.share.vm.oops;

import com.cover.jvm.hotspot.src.share.vm.interpreter.BytecodeStream;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CodeAttributeInfo {
    private int attrNameIndex;
    
    private int attrLength;
    
    private int maxStack;
    
    private int maxLocals;
    
    private int codeLength;
    
    private BytecodeStream code;
    
    private int exceptionTableLength;
    
    // 如局部变量、操作数栈
    private int attributesCount;
    
    private Map<String, AttributeInfo> attributes = new HashMap<>();

    @Override
    public String toString() {
        return "CodeAttributeInfo{ }";
    }
}
