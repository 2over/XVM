package com.cover.jvm.example.java.lang.operation;


public class Sub {

    public static void main(String[] args) {
        subInt();
        subFloat();
        subLong();
        subDouble();
    }

    public static void subInt() {
        int a = 1;
        int b = 2;

        System.out.println(a - b);
    }

    public static void subFloat() {
        float a = 1;
        float b = 2;

        System.out.println(a - b);
    }

    public static void subLong() {
        long a = 1;
        long b = 2;

        System.out.println(a - b);
    }

    public static void subDouble() {
        double a = 1;
        double b = 2;

        System.out.println(a - b);
    }

}
