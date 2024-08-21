package com.cover.jvm.example.java.lang.condition;


public class IfOther {

    public static void main(String[] args) {
        IntVSFloat();
    }

    public static void floatCompare() {
        float f = 10;

        if (10 == f) {
            System.out.println("相等");
        } else {
            System.out.println("不相等");
        }

        if (f == 10) {
            System.out.println("相等");
        } else {
            System.out.println("不相等");
        }

        if (f > 10) {
            System.out.println("大于10");
        } else {
            System.out.println("不大于10");
        }
    }

    public static void IntVSFloat() {
        int i = 10;
        float f = 10;

        if (i == f) {
            System.out.println("相等");
        } else {
            System.out.println("不相等");
        }
    }
}
