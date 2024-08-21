package com.cover.jvm.example.java.lang.condition;


public class IfInt {

    public static void main(String[] args) {
        ifeq();
        ifne();

        ifge();
        ifgt();

        ifle();
        iflt();
    }

    public static void ifeq() {
        int a = 0;

        if (a != 0) {
            System.out.println("不相等");
        } else {
            System.out.println("相等");
        }
    }

    public static void ifne() {
        int a = 0;

        if (a == 0) {
            System.out.println("相等");
        } else {
            System.out.println("不相等");
        }
    }

    public static void iflt() {
        int a = 0;

        if (a < 0) {
            System.out.println("a<0");
        } else {
            System.out.println("a不<0");
        }
    }

    public static void ifle() {
        int a = 0;

        if (a <= 0) {
            System.out.println("a<=0");
        } else {
            System.out.println("a不<=0");
        }
    }

    public static void ifgt() {
        int a = 0;

        if (a > 0) {
            System.out.println("a>0");
        } else {
            System.out.println("a不>0");
        }
    }

    public static void ifge() {
        int a = 0;

        if (a >= 0) {
            System.out.println("a>=0");
        } else {
            System.out.println("a不>=0");
        }
    }

    /**
     * 这两种写法生成的字节码不一样
     */
    public static void test() {
        int a = 0;

        if (a == 0) {

        }

        if (0 == a) {

        }
    }
}
