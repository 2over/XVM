package com.cover.jvm.hotspot.src.share.vm.runtime;

import com.cover.jvm.hotspot.src.share.vm.utilities.BasicType;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Stack;

@Data
public class StackValueCollection {
    
    private Logger logger = LoggerFactory.getLogger(StackValueCollection.class);
    
    private Stack<StackValue> container = new Stack<>();
    
    private StackValueCollection() {
        
    }
    
    public void push(StackValue value) {
        getContainer().push(value);
    }
    
    public StackValue pop() {
        return getContainer().pop();
    }
    
    public StackValue peek() {
        return getContainer().peek();
    }

    /**
     * 一个double用两个int存储
     * @param val
     */
    public void pushDouble(double val) {
        byte[] bytes = DataTranslate.doubleToBytes(val);
        ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, 0);
        push(new StackValue(BasicType.T_DOUBLE, buffer.getInt(0)));
        // 为了后面取数据
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        push(new StackValue(BasicType.T_DOUBLE, buffer.getInt(4)));
        
    }

    /**
     * 因为一个double占两个单元
     * 所以取的时候要连续取两个，并合并成double
     * @return
     */
    public double popDouble() {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        StackValue value1 = pop(); // 后4个字节
        StackValue value2 = pop(); // 前4个字节
        if (value1.getType() != BasicType.T_DOUBLE || value2.getType() != BasicType.T_DOUBLE) {
            throw new Error("类型检查不通过");
        }
        
        buffer.putInt(value2.getVal(), value1.getVal());
        return buffer.getDouble();
    }
    
    public StackValue[] popDouble2() {
        StackValue[] ret = new StackValue[2];
        ret[0] = pop();
        ret[1] = pop();
        return ret;
    }
    
    public void pushNull(JavaVFrame frame) {
        frame.getStack().push(new StackValue(BasicType.T_OBJECT, null));
    }
    
    public void pushArray(ArrayOop array, JavaVFrame frame) {
        frame.getStack().push(new StackValue(BasicType.T_ARRAY, array));
    }
    
    public ArrayOop popArray(JavaVFrame frame) {
        StackValue value = frame.getStack().pop();
        if (BasicType.T_ARRAY != value.getType()) {
            throw new Error("类型检查不通过");
        }
        
        return (ArrayOop) value.getObject();
    }
    
    public void pushInt(int val, JavaVFrame frame) {
        frame.getStack().push(new StackValue(BasicType.T_INT, val));
    }
    
    ////////////////////////////////////////模拟局部变量表
    private int maxLocals;
    
    private int index;
    
    private StackValue[] locals;
    
    public StackValueCollection(int size) {
        maxLocals = size;
        locals = new StackValue[size];
    }
    
    public void smartAdd(int index, StackValue value) {
        getLocals()[index] = value;

        /**
         * 如果之前的index = 2
         * 这时候指定赋值了index = 5的位置，那后面就从6开始，而不会从3开始
         */
        this.index += index;
    }
    
    public void add(int index, StackValue value) {
        getLocals()[index] = value;
    }
    
    public StackValue get(int index) {
        return getLocals()[index];
    }
    
}
