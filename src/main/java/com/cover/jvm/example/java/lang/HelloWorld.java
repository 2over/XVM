package com.cover.jvm.example.java.lang;

public class HelloWorld {
    
    private static final int a = 1;
    
    private static final int b = 2;
    public static void main(String[] args) {
        System.out.println("Hello Wolrd!");

        System.out.println(add(a, b));
    }
    
    public static int add(int a, int b) {
        return a + b;
    }
}
