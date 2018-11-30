package org.yeyu.simpletest;

import java.util.concurrent.atomic.AtomicInteger;

public class Idea1 {
    public static void main(String... args) {
        Idea1 idea = new Idea1();
        idea.test1();
        idea.test2();
        idea.test3();
    }

    void test1() {
        int x = 2;
        work1(x);
        System.out.println(x);
    }

    private void work1(int x) {
        x = 3;
        System.out.println(x);
    }

    void test2() {
        String y = "hello world";
        work2(y);
        System.out.println(y);
    }

    private void work2(String y) {
        y.substring(6);
        System.out.println(y);
    }

    void test3() {
        AtomicInteger z = new AtomicInteger(5);
        work3(z);
        System.out.println(z.get());
    }

    private void work3(AtomicInteger z) {
        z.getAndIncrement();
        System.out.println(z.get());
    }
}
