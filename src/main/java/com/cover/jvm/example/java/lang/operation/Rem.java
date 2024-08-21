package com.cover.jvm.example.java.lang.operation;


public class Rem {

    public static void main(String[] args) {
        remInt();
        remFloat();
        remLong();
        remDouble();
    }

    public static void remInt() {
        int a = 1;
        int b = 2;

        System.out.println(a % b);
    }

    public static void remFloat() {
        float a = 1;
        float b = 2;

        System.out.println(a % b);
    }

    public static void remLong() {
        long a = 1;
        long b = 2;

        System.out.println(a % b);
    }

    public static void remDouble() {
        double a = 1;
        double b = 2;

        System.out.println(a % b);
    }

}
