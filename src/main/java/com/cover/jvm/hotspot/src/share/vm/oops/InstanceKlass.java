package com.cover.jvm.hotspot.src.share.vm.oops;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class InstanceKlass {
    
    private byte[] magic = new byte[4];
    
    private byte[] minorVersion = new byte[2];
    
    private byte[] majorVersion = new byte[2];
    
    private ConstantPool constantPool;
    
    private int accessFlag;
    
    private int thisClass;
    
    private int superClass;
    
    private int interfacesLength;
    
    private List<InterfaceInfo> interfaceInfos = new ArrayList<>();
    
    private int fieldsLength;
    
    private List<FieldInfo> fields = new ArrayList<>();
    
    private int methodLength;
    
    private MethodInfo[] methods;
    
    private int attributeLength;
    
    private Map<String, AttributeInfo> attributeInfos = new HashMap<>();
    
    public BootstrapMethods getBootstrapMethods() {
        return (BootstrapMethods)attributeInfos.get("BootstrapMethods");
    }
    
    public InstanceKlass() {
        constantPool = new ConstantPool();
        
        constantPool.setKlass(this);
    }
    
    public void initMethodsContainer() {
        methods = new MethodInfo[methodLength];
    }

    @Override
    public String toString() {
        return "InstanceKlass{ }";
    }
}
