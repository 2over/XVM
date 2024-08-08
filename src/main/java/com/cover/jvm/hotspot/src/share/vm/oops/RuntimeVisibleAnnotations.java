package com.cover.jvm.hotspot.src.share.vm.oops;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RuntimeVisibleAnnotations extends AttributeInfo {
    
    private int annotationsNum;
    
    private List<Annotation> annotations = new ArrayList<>();
    
    private String attrName;
}
