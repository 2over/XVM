package com.cover.jvm.example.java.lang.lambda;


public class TestLambda {

    public static void main(String[] args) {
        CustomLambda2 obj = (x, y) -> {
            System.out.println("hello#" + x + "#" + y);
        };

        obj.run(1, 2);

        //====
        CustomLambda1 obj1 = (x) -> {
            System.out.println("hello#" + x);
        };

        obj1.run(1);
    }
}
