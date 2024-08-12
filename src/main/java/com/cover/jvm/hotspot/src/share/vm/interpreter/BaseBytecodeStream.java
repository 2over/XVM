package com.cover.jvm.hotspot.src.share.vm.interpreter;

import com.cover.jvm.hotspot.src.share.tools.DataTranslate;
import com.cover.jvm.hotspot.src.share.tools.Stream;
import com.cover.jvm.hotspot.src.share.vm.memory.StackObj;
import com.cover.jvm.hotspot.src.share.vm.oops.CodeAttributeInfo;
import com.cover.jvm.hotspot.src.share.vm.oops.MethodInfo;
import lombok.Data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Data
public class BaseBytecodeStream extends StackObj {

    protected MethodInfo belongMethod;

    protected CodeAttributeInfo belongCode;

    protected int length;

    protected int index;

    protected byte[] codes;

    /**
     * 一次取一字节数据，自动累加
     *
     * @return
     */
    public int getU1Code() {
        if (index < 0 || index >= length) {
            throw new Error("字节码指令的索引超过了最大值");
        }
        return Byte.toUnsignedInt(codes[index++]);
    }

    public int getU1Code2() {
        if (index < 0 || index >= length) {
            throw new Error("字节码指令的索引超过了最大值");
        }
        
        return codes[index++];
    }
    
    public short getUnsignedShort() {
        if (index < 0 || index >= length) {
            throw new Error("字节码指令的索引超过了最大值");
        }
        
        byte[] u2Arr = new byte[2];

        Stream.readU2Simple(codes, index, u2Arr);
        index += 2;
        
        return (short) DataTranslate.byteToUnsignedShort(u2Arr);
    }
    
    public int getU4Code() {
        if (index < 0 || index >= length) {
            throw new Error("字节码指令的索引超过了最大值");
        }
        
        byte[] arr = new byte[4];
        
        Stream.readU4Simple(codes, index, arr);
        index += 4;
        ByteBuffer buffer = ByteBuffer.wrap(arr);
        
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        
        return buffer.getInt();
    }

    public void reset() {
        index = 0;
    }
    
    public boolean end() {
        return index >= length;
    }
    
    public void inc(int step) {
        index += step;
    }

}
