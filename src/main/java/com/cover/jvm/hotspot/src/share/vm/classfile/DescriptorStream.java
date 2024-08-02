package com.cover.jvm.hotspot.src.share.vm.classfile;

import com.cover.jvm.hotspot.src.share.vm.memory.ResourceObj;
import com.cover.jvm.hotspot.src.share.vm.oops.DescriptorInfo;
import com.cover.jvm.hotspot.src.share.vm.utilities.BasicType;
import lombok.Data;

import java.util.List;

@Data
public class DescriptorStream extends ResourceObj {
    
    private byte[] container;
    
    private DescriptorInfo info = new DescriptorInfo();
    
    public DescriptorStream(String descriptor) {
        container = descriptor.getBytes();
    }
    
    public DescriptorInfo parse() {
        for (int i = 0; i < container.length; i++) {
            byte b = container[i];
            
            switch (b) {
                case '[': {
                    String s = parseArrayType();
                    info.setType(BasicType.T_ARRAY);
                    info.setTypeDesc(s);
                    return info; 
                }
                case 'L': {
                    String s = parseReferenceType();
                    info.setType(BasicType.T_OBJECT);
                    info.setTypeDesc(s);
                    return info;
                }
                case 'Z': { // boolean
                    info.setType(BasicType.T_BOOLEAN);
                    return info;
                }
                case 'B': { // byte
                    info.setType(BasicType.T_BYTE);
                    return info;
                }
                case 'C': { // char
                    info.setType(BasicType.T_CHAR);
                    return info;
                }
                case 'I' : { // int
                    info.setType(BasicType.T_INT);
                    return info;
                }
                case 'F': { // float
                    info.setType(BasicType.T_FLOAT);
                    return info;
                }
                case 'J': { // long
                    info.setType(BasicType.T_LONG);
                    return info;
                }
                case 'D': { // double
                    info.setType(BasicType.T_DOUBLE);
                    return info;
                }
                case 'V': {
                    info.setType(BasicType.T_VOID);
                    return info;
                }
                default:
                    throw new Error("无法识别的类型");
            } /* end switch */
        }
        return info;
    }
    
    private String parseArrayType() {
        for (int i = 0; i < container.length; i++) {
            // 取出数组维度
            if ('[' == container[i]) {
                info.incArrayDimension();
                continue;
            }
            
            if ('L' != container[i]) {
                return null;
            }
            
            return parseArrReferenceType();
        }
        
        return null;
    }
    
    private String parseArrReferenceType() {
        // 跳过数组维度个[、开头的L、结尾;
        int size = container.length - info.getArrayDimension() - 2;
        byte[] str = new byte[size];
        
        // 跳过开头的L、结尾的;
        int j = 0;

        /**
         * 读的位置向后推移了info.getArrayDimension() + 1
         * 那就该多读这么多次
         */
        for (int i = info.getArrayDimension() + 1; i < size + 1 + info.getArrayDimension(); i++) {
            str[j++] = container[i];
        }
        
        return new String(str);
    }
    
    private String parseReferenceType() {
        // 跳过开头的L、结尾的
        int size = container.length - 2;
        byte[] str = new byte[size];
        
        // 跳过开头的L、结尾的;
        int j = 0;
        for (int i = 1; i < container.length - 1; i++) {
            str[j++] = container[i];
        }
        return new String(str);
    }
    
    public void parseParams(List<DescriptorInfo> infos) {
        for (int i = 0; i < container.length; i++) {
            byte b = container[i];
            switch (b) {
                case 'Z': // boolean
                    infos.add(new DescriptorInfo(true, BasicType.T_BOOLEAN));
                    break;
                case 'B': // byte
                    infos.add(new DescriptorInfo(true, BasicType.T_BYTE));
                    break;
                case 'C': // char
                    infos.add(new DescriptorInfo(true, BasicType.T_CHAR));
                    break;
                case 'I': // int
                    infos.add(new DescriptorInfo(true, BasicType.T_INT));
                    break;
                case 'F': // float
                    infos.add(new DescriptorInfo(true, BasicType.T_FLOAT));
                    break;
                case 'J': // long
                    infos.add(new DescriptorInfo(true, BasicType.T_LONG));
                    break;
                case 'D': //double
                    infos.add(new DescriptorInfo(true,BasicType.T_DOUBLE));
                    break;
                case 'V': 
                    infos.add(new DescriptorInfo(true, BasicType.T_VOID));
                    break;
                default:
                    throw new Error("无法识别的类型");
            } /* end switch */
        }
    }

}
