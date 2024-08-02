package com.cover.jvm.hotspot.src.share.tools;

public class Stream {

    /**
     * 
     * @param content
     * @param from  从哪个位置开始读
     * @param ret 传出参数
     * @return 读完之后指针的位置，即开始读的位置 + 读了多少
     */
    public static int readU2(byte[] content, int from, byte[] ret)  {
        System.arraycopy(content, from ,ret, 0, 2);
        return from + 2;
    }
    
    public static void readU2Simple(byte[] content, int from, byte[] ret) {
        System.arraycopy(content, from, ret, 0, 2);
    }
    
    public static void readSimple(byte[] content, int from, int size, byte[] ret) {
        System.arraycopy(content, from, ret, 0, size);
    }
    
    public static byte readU1Simple(byte[] content, int from) {
        byte[] arr = new byte[1];
        System.arraycopy(content, from, arr, 0, 1);
        return arr[0];
    }
    
    
    public static int readU4(byte[] content, int from, byte[] ret) {
        System.arraycopy(content, from, ret, 0 ,4);
        return from + 4;
    }
    
    public static void readU4Simple(byte[] content, int from, byte[] ret) {
        System.arraycopy(content, from,ret, 0, 4);
    }
    
    public static int readU1(byte[] content, int from, byte[] ret) {
        System.arraycopy(content, from, ret, 0, 1);
        return from + 1;
    }
    
    public static int readU8(byte[] content, int from, byte[] ret) {
        System.arraycopy(content, from ,ret, 0, 8);
        return from + 8;
    }
    
    public static void readU8Simple(byte[] content, int from, byte[] ret) {
        System.arraycopy(content, from, ret, 0 ,8);
    }
    
    public static String toHex(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int)s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        
        return str;
    }
    
    public static int byteToUnsignedShort(byte[] bytes, int off) {
        int high = bytes[off];
        int low = bytes[off + 1];
        
        return (high << 8 & 0xFF00) | (low & 0xFF);
    }
    
    public static byte[] unsignedShortToByte(int s) {
        byte[] targets = new byte[2];
        targets[0] = (byte) (s >> 8 & 0xFF);
        targets[1] = (byte) (s & 0xFF);
        return targets;
    }
    
    
}
