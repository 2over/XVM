package com.cover.jvm.hotspot.src.share.vm.runtime;

import com.cover.jvm.hotspot.src.share.vm.memory.AllStatic;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Threads  extends AllStatic {


    /**
     * 所有的Java线程基本全部存储在这个list中
     */
    private static List<Thread> threadList;
    
    private static Thread currentThread;
    
    static {
        threadList = new ArrayList<>();
    }
    
    public static List<Thread> getThreadList() {
        return threadList;
    }
    
    public static JavaThread currentThread() {
        return (JavaThread) currentThread;
    }
    
    public static void setCurrentThread(Thread thread) {
        currentThread = thread;
    }
}
