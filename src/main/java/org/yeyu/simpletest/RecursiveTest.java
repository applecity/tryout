package org.yeyu.simpletest;

public class RecursiveTest {
    public static void main(String... args) {
        System.out.println(factorialRecursive(5));
        System.out.println(factorialTailRecursive(5));
    }

    static long factorialTailRecursive(long n) {
        return factorialHelper(1, n);
    }

    static long factorialHelper(long acc, long n) {
        return n == 1 ? acc : factorialHelper(acc * n, n-1);
    }

    static long factorialRecursive(long n) {
        return n == 1 ? 1 : n * factorialRecursive(n-1);
    }
}
