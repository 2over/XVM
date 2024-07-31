package com.cover.jvm.hotspot.src.share.tools;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DataTranslate {

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    
    public static String bytesToHexFun1(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int a = 0;
        int index = 0;
        for (byte b : bytes) {
            if (b < 0) {
                a = 256 + b;
            } else {
                a = b;
            }
            
            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }
        return new String(buf);
    }
    
    public static String bytesTOHexFun2(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        
        for (byte b : bytes) {
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }
        
        return new String(buf);
    }
    
    public static String bytesToHexFun3(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }
        return buf.toString();
    }
    
    public static byte[] toBytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }
        
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring( i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        
        return bytes;
    }
    
    public static int byteToUnsignedShort(byte[] bytes) {
        int high = bytes[0];
        int low = bytes[1];
        return (high << 8 & 0xFF00) | (low & 0xFF);
    }
    
    public static byte[] unsignedShortToByte(int s) {
        byte[] targets = new byte[2];
        targets[0] = (byte) (s >> 8 & 0xFF);
        targets[1] = (byte)(s & 0xFF);
        return targets;
    }

    /**
     * int到byte[] 由高位到低位
     * @param i 需要转换为byte数组的整行值
     * @return byte数组
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        
        return result;
    }

    /**
     * byte[]转int
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (3 -i) * 8;
            value += (bytes[i] & 0xFF) << shift;
        }
        
        return value;
    }
    
    public static float byteToFloat(byte[] b) {
        int l;
        l = b[3];
        l &= 0xff;
        l |= ((long)b[2] << 8);
        l &= 0xffff;
        l |= ((long)b[1] << 16);
        l &= 0xffffff;
        l |= ((long) b[0] << 24);
        return Float.intBitsToFloat(l);
    }
    
    public static float bytesToFloat(byte[] arr, boolean littleEndian) {
        ByteBuffer buffer = ByteBuffer.wrap(arr, 0 ,4);
        if (littleEndian) {
            buffer.order(ByteOrder.LITTLE_ENDIAN);
        }
        
        return buffer.getFloat();
    }
    
    public static byte[] floatToByte(float f) {
        byte[] ret =  new byte[4];
        // 将float里面的二进制串解释为int整数
        int i = Float.floatToIntBits(f);
        ret[0] = (byte) ((i & 0xff000000) >> 24);
        ret[1] = (byte) ((i & 0x00ff0000) >> 16);
        ret[2] = (byte) ((i & 0x0000ff00) >> 8);
        ret[3] = (byte) (i & 0x000000ff);
        return ret;
    }
    
    public static Long byteToLong(byte[] input, int offset, boolean littleEndian) {
        long value = 0;
        // 循环读取每个字节通过移位运算完成long的8个字节拼接
        for (int count = 0; count < 8; ++count) {
            int shift = (littleEndian ? count : (7 - count)) << 3;
            value |= ((long)0xff << shift) & ((long)input[offset + count] << shift);
        }
        
        return value;
    }
    
    public static long bytes2long(byte[] bs) throws Exception {
        int bytes = bs.length;
        if (bytes > 1) {
            if ((bytes % 2) != 0 || bytes > 8) {
                throw new Exception("not support");
            }
        }
        switch (bytes) {
            case 0:
                return 0;
            case 1:
                return (long)((bs[0] & 0xff));
            case 2:
                return (long)((bs[0] & 0xff) << 8 | (bs[1] & 0xff));
            case 4:
                return (long)((bs[0] & 0xffL) << 24 | (bs[1] & 0xffL) << 16 | (bs[2] & 0xffL) << 8 | (bs[3] & 0xffL));
            case 8:
                return (long)((bs[0] & 0xffL) << 56 | (bs[1] & 0xffL) << 48 | (bs[2] & 0xffL) << 40 | (bs[3] & 0xffL) << 32
                        | (bs[4] & 0xffL) << 24 | (bs[5] & 0xffL) << 16 | (bs[6] & 0xffL) << 8 | (bs[7] & 0xffL));
            default:
                throw new Exception("not support");
        }
    }

    /**
     * 是否是小端模式
     */
    public static double byteToDouble(byte[] arr, boolean littleEndian) {
        ByteBuffer buffer = ByteBuffer.wrap(arr, 0, 8);
        if (littleEndian) {
            buffer.order(ByteOrder.LITTLE_ENDIAN);
        }
        return buffer.getDouble();
    }
    
    public static byte[] doubleToBytes(double d) {
        long value = Double.doubleToRawLongBits(d);
        byte[] byteRet = new byte[8];
        for (int i = 0; i < 8; i++) {
            byteRet[i] = (byte)((value >> 8 * i) & 0xff);
        }
        
        return byteRet;
    }
    
    public static byte[] longToBytes(long v) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(v);
        return buffer.array();
    }
    
    public static long bytesToLong(byte[] arr) {
        ByteBuffer buffer = ByteBuffer.wrap(arr, 0, 8);
        return buffer.getLong();
    }
    
    
}
