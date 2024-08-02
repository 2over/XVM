package com.cover.jvm.jdk.classes;

public final class Handle {
    
    // oop、klass的内存地址
    private long p;

    /**
     * 1.klass
     * 2.oop
     * 3.method
     */
    private int type;
    
    // 对应的Java类的全限定名
    private String className;
    
    public long getP() {
        return p;
    }
    
    public void setP(long p) {
        this.p = p;
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
}
