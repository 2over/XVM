package com.cover.jvm.hotspot.src.share.vm.oops;

import lombok.Data;

@Data
public class LineNumberTable extends AttributeInfo{
    
    private int tableLength;
    
    private Item[] table;
    
    public void initTable() {
        table = new Item[tableLength];
    }
    
    @Data
    public class Item {
        private int startPc;
        
        private int lineNumber;
    }
}
