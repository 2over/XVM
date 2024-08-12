package com.cover.jvm.hotspot.src.share.vm.interpreter;

import com.cover.jvm.hotspot.src.share.vm.oops.CodeAttributeInfo;
import com.cover.jvm.hotspot.src.share.vm.oops.MethodInfo;

public class BytecodeStream extends BaseBytecodeStream{
    
    public BytecodeStream(MethodInfo belongMethod, CodeAttributeInfo belongCode) {
        this.belongMethod = belongMethod;
        this.belongCode = belongCode;
        this.length = belongCode.getCodeLength();
        this.index = 0;
        this.codes = new byte[this.length];
    }
}
