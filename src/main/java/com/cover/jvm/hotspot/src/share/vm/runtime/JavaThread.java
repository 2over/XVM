package com.cover.jvm.hotspot.src.share.vm.runtime;

import lombok.Data;

import java.util.Stack;

@Data
public class JavaThread extends Thread{
    
    private Stack<VFrame> stack = new Stack<>();
}
