package com.cover.jvm.hotspot.src.share.vm.oops;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ArrayOop {

    /**
     * 数组类型
     */
    private int type;

    /**
     * 如果是引用类型数组，数组元素对应的类型
     */
    private String className;

    /**
     * 数组大小
     */
    private int size;
    
    private List<Object> data;
    
    public ArrayOop(int type, int size) {
        this.type = type;
        this.size = size;
        
        data = new ArrayList<>(size);
    }
    
    public ArrayOop(int type, String name, int size) {
        this.type = type;
        this.className = name;
        this.size = size;
        data = new ArrayList<>(size);
    }

    @Override
    public String toString() {
        return "ArrayOop{" +
                "type=" + type +
                ", size=" + size +
                '}';
    }
}
