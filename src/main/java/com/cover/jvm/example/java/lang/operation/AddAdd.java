package com.cover.jvm.example.java.lang.operation;


public class AddAdd {

    public static void main(String[] args) {
        byteAdd();
        intAdd();
        longAdd();
        floatAdd();
        doubleAdd();
        doubleAdd2();
    }

    public static void intAdd() {
        int v = 1;
        System.out.println(v++);
        System.out.println(++v);

        System.out.println(--v);
    }

    //=====
    public static void byteAdd() {
        byte v = 1;
        System.out.println(v++);
        System.out.println(++v);
    }

    public static void floatAdd() {
        float v = 1;
        System.out.println(v++);
        System.out.println(++v);
    }

    public static void longAdd() {
        long v = 1;
        System.out.println(v++);
        System.out.println(++v);
    }

    public static void doubleAdd() {
        double v = 1;
        System.out.println(v++);
        System.out.println(++v);
    }

    public static void doubleAdd2() {
        double v = 1;
        System.out.println(++v);
    }
}
