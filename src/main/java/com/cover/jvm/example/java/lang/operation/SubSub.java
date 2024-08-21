package com.cover.jvm.example.java.lang.operation;


public class SubSub {

    public static void main(String[] args) {
        test1();
        test2();
    }

    public static void test1() {
        int v = 1;
        System.out.println(v--);
    }

    public static void test2() {
        int v = 1;
        System.out.println(--v);
    }
}
