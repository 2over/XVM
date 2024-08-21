package com.cover.jvm.example.java.lang.operation;


public class Div {

    public static void main(String[] args) {
        divInt();
        divFloat();
        divLong();
        divDouble();
    }

    public static void divInt() {
        int a = 4;
        int b = 2;

        System.out.println(a / b);
    }

    public static void divFloat() {
        float a = 4;
        float b = 2;

        System.out.println(a / b);
    }

    public static void divLong() {
        long a = 4;
        long b = 2;

        System.out.println(a / b);
    }

    public static void divDouble() {
        double a = 4;
        double b = 2;

        System.out.println(a / b);
    }

}
