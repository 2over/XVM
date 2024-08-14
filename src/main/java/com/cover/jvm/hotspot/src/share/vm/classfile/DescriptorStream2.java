package com.cover.jvm.hotspot.src.share.vm.classfile;

import com.cover.jvm.hotspot.src.share.vm.memory.ResourceObj;
import com.cover.jvm.hotspot.src.share.vm.oops.DescriptorInfo;
import com.cover.jvm.hotspot.src.share.vm.runtime.JavaVFrame;
import com.cover.jvm.hotspot.src.share.vm.runtime.StackValue;
import com.cover.jvm.hotspot.src.share.vm.utilities.BasicType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DescriptorStream2 extends ResourceObj {
    
    private String descriptor;
    
    private DescriptorInfo returnElement;
    
    private int methodParamsSize;
    
    private List<DescriptorInfo> parameters = new ArrayList<>();
    
    public DescriptorStream2(String descriptor) {
        this.descriptor = descriptor;
    }
    
    
    public void parseMethod() {
        parseMethodParams();
        parseReturn();
    }
    
    public void parseField() {
        returnElement = new DescriptorStream(descriptor).parse();
    }
    
    public DescriptorInfo parseReturn() {
        int paramEndIndex = descriptor.indexOf(")");
        String returnStr = descriptor.substring(paramEndIndex + 1, descriptor.length());
        returnElement = new DescriptorStream(returnStr).parse();
        return returnElement;
    }
    
    public List<DescriptorInfo> parseMethodParams() {
        int paramStartIndex = descriptor.indexOf('(');
        int paramEndIndex = descriptor.indexOf(')');

        String paramStr = descriptor.substring(paramStartIndex + 1, paramEndIndex);
        String[] strings = paramStr.split(";");
        if (strings.length > 0) {
            for (int i = 0; i < strings.length; i++) {
                String s = strings[i];
                
                // 处理字符串或数组
                if (s.startsWith("L") || s.startsWith("[")) {
                    // 保持格式。 共用解析返回值的程序
                    s += ";";
                    
                    parameters.add(new DescriptorStream(s).parse());
                    
                    continue;
                }
                
                // 处理其他
                new DescriptorStream(s).parseParams(parameters);
            }
        }
        
        setMethodParamsSize(parameters.size());
        return parameters;
    }
    
    
    public Class<?>[] getParamsType() {
        Class<?>[] types = new Class[getMethodParamsSize()];
        for (int i = 0; i < getMethodParamsSize(); i++) {
            DescriptorInfo info = getParameters().get(i);
            switch (info.getType()) {
                case BasicType.T_BOOLEAN:
                    types[i] = boolean.class;
                    break;
                case BasicType.T_CHAR:
                    types[i] = char.class;
                    break;
                case BasicType.T_INT:
                    types[i] = int.class;
                    break;
                case BasicType.T_OBJECT:
                    types[i] = Class.forName(info.getTypeDesc().replace("/", "."));
                    break;
                case BasicType.T_LONG:
                    types[i] = long.class;
                    break;
                case BasicType.T_DOUBLE:
                    types[i] = double.class;
                    break;
                case BasicType.T_FLOAT:
                    types[i] = float.class;
                    break;
                case BasicType.T_ARRAY:
                    throw new Error("数组类型，未作处理");
                default:
                    throw new Error("无法识别的参数类型");
            }
            
        }
        
        return types;
    }
    
    public Object[] getParamsVal(JavaVFrame frame) {
        Object[] vals = new Object[getMethodParamsSize()];
        for (int i = 0; i < getMethodParamsSize(); i++) {
            DescriptorInfo info = getParameters().get(i);
            
            switch (info.getType()) {
                case BasicType.T_BOOLEAN:
                    int val = frame.getStack().pop().getVal();
                    vals[i] = (1 == val) ? true: false;
                    
                    break;
                case BasicType.T_CHAR:
                    /**
                     * 这里一定要强转
                     * 否则接受的参数是char，传int会报错
                     */
                    vals[i] = (char)frame.getStack().pop().getVal();
                    break;
                case BasicType.T_INT:
                    vals[i] = frame.getStack().pop().getVal();
                    break;
                case BasicType.T_OBJECT:
                    vals[i] = frame.getStack().pop().getObject();
                    break;
                case BasicType.T_LONG:
                    vals[i] = frame.getStack().pop().getData();
                    break;
                case BasicType.T_DOUBLE:
                    vals[i] = frame.getStack().popDouble();
                    break;
                case BasicType.T_FLOAT:
                    vals[i] = frame.getStack().pop().getData();
                    break;
                case BasicType.T_ARRAY:
                    throw new Error("数组类型， 未作处理");
                default:
                    throw new Error("无法识别的参数类型");
            }
        }
        
        return vals;
    }
    
    public Class<?> getFieldType() {
        Class<?> type = null;
        switch (returnElement.getType()) {
            case BasicType.T_CHAR:
                type = char.class;
                break;
            case BasicType.T_INT:
                type = int.class;
                break;
            case BasicType.T_OBJECT:

                try {
                    type = Class.forName(returnElement.getTypeDesc().replace('/', '.'));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case BasicType.T_LONG:
                type = long.class;
                break;
            case BasicType.T_DOUBLE:
                type = double.class;
                break;
            case BasicType.T_ARRAY:
                throw new Error("数组类型，未作处理");
            default:
                throw new Error("无法识别的参数类型");
        }
        
        return type;
    }
    
    public Object getFieldVal(JavaVFrame frame) {
        Object val = null;

        switch (returnElement.getType()) {
            case BasicType.T_CHAR: {
                val = frame.getStack().pop().getVal();

                break;
            }
            case BasicType.T_INT: {
                val = frame.getStack().pop().getVal();

                break;
            }
            case BasicType.T_OBJECT: {
                val = frame.getStack().pop().getObject();

                break;
            }
            case BasicType.T_LONG: {
                val = frame.getStack().popDouble();
                break;
            }
            case BasicType.T_DOUBLE: {
                val = frame.getStack().popDouble();

                break;
            }
            case BasicType.T_ARRAY: {
                throw new Error("数组类型, 未作处理");
            }
            default:
                throw new Error("无法识别的参数类型");

        }
        return val;

    }
    
    public void pushField(Object o, JavaVFrame frame) {
        switch (returnElement.getType()) {
            case BasicType.T_CHAR:
                frame.getStack().push(new StackValue(BasicType.T_CHAR, (char)o));
                break;
            case BasicType.T_INT:
                frame.getStack().push(new StackValue(BasicType.T_INT, (int)o));
                break;
            case BasicType.T_OBJECT:
                frame.getStack().push(new StackValue(BasicType.T_OBJECT, o));
                break;
            case BasicType.T_LONG:
                frame.getStack().push(new StackValue(BasicType.T_LONG, (int)o));
                break;
            case BasicType.T_DOUBLE:
                frame.getStack().push(new StackValue(BasicType.T_DOUBLE, (int)o));
                break;
            case BasicType.T_ARRAY:
                throw new Error("数组类型，未作处理");
            default:
                throw new Error("无法识别的参数类型");
        }
    }
    
}
