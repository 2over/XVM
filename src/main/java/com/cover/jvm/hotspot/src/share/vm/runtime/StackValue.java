package com.cover.jvm.hotspot.src.share.vm.runtime;

import com.cover.jvm.hotspot.src.share.tools.DataTranslate;
import com.cover.jvm.hotspot.src.share.vm.utilities.BasicType;
import lombok.Data;

@Data
public class StackValue {
    
    private int type;

    /**
     * 数据存储再这里的情况
     * 1.float
     */
    private byte[] data;

    /**
     * 数据存储再这里的情况
     * 1.int
     * 2.short
     * 3.char
     * 4.byte
     * 5.double用两个int
     */
    private int val;
    
    private Object object;
    
    public StackValue(int type, int val) {
        this.type = type;
        this.val = val;
    }
    
    public StackValue(int type, Object val) {
        this.type = type;
        this.object = val;
    }
    
    public StackValue(int type, float val) {
        this.type = type;
        this.data = DataTranslate.floatToByte(val);
    }
    
    public StackValue(int type, long v) {
        this.type = type;
        this.data = DataTranslate.longToBytes(v);
    }
    public Object getData() {
        switch (type) {
            case BasicType.T_OBJECT:
                return object;
            case BasicType.T_FLOAT:
                return DataTranslate.byteToFloat(data);
            case BasicType.T_LONG:
                return DataTranslate.bytesToLong(data);
            case BasicType.T_BYTE:
            case BasicType.T_INT:
                return val;
            case BasicType.T_ADDRESS:
                return DataTranslate.bytesToLong(data);
        }
        
        return null;
    }
}
