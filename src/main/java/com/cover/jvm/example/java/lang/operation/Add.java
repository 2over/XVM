package com.cover.jvm.example.java.lang.operation;


public class Add {

    public static void main(String[] args) {
        addInt();
        addFloat();
        addLong();
        addDouble();
        add();
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

    public static void add() {
        byte b = 1;
        char c = 2;
        short s = 3;
        int i = 4;
        long l = 5;

        System.out.println(b + c + s + i);
    }

    public static void test1() {
        byte b = 1;
        char c = 2;

        System.out.println(b + c);
    }

}
