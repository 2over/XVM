package com.cover.jvm.hotspot.src.share.vm.oops;

import com.cover.jvm.hotspot.src.share.vm.utilities.AccessFlags;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InnerClasses extends AttributeInfo {
    
    private String attrName;
    
    private int numOfClasses;
    
    private List<Item> classes = new ArrayList<>();
    
    
    @Data
    public class Item {
        private int interClassInfoIndex;
        
        private int outerClassInfoIndex;
        
        private int innerNameIndex;
        
        private AccessFlags accessFlags;
    }
}
