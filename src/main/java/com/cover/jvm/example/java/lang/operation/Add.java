package com.cover.jvm.example.java.lang.operation;

/**
 * Created By ziya
 * QQ: 3039277701
 * 2021/4/6
 */
public class Add {

    public static void main(String[] args) {
        addInt();
        addFloat();
        addLong();
        addDouble();
    }

    public static void addInt() {
        int a = 1;
        int b = 2;

        System.out.println(a + b);
    }

    public static void addFloat() {
        float a = 1;
        float b = 2;

        System.out.println(a + b);
    }

    public static void addLong() {
        long a = 1;
        long b = 2;

        System.out.println(a + b);
    }

    public static void addDouble() {
        double a = 1;
        double b = 2;

        System.out.println(a + b);
    }

}
