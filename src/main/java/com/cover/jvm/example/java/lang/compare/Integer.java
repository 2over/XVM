package com.cover.jvm.example.java.lang.compare;


public class Integer {

    public static void main(String[] args) {
        testLong();
    }

    public static void test1() {
        byte v1 = 10;
        byte v2 = 20;

        System.out.println(v1 == v2);
    }

    public static void testLong() {
        long v1 = 10;
        long v2 = 20;

        System.out.println(v1 != v2);
    }

    public static void test() {
        Object obj = null;

        System.out.println(obj == null);
    }
}
