package com.cover.jvm.hotspot.src.share.vm.oops;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RuntimeInvisibleAnnotations extends AttributeInfo {
    
    private int annotationsNum;
    
    private List<Annotation> annotations = new ArrayList<>();
    
    private String attrName;
}
