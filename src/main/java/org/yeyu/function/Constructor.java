package org.yeyu.function;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class Constructor {
    void s1() {
        UnaryOperator<String> op1 = s -> s+"...1";
        UnaryOperator<String> op2 = s -> s+"...2";
        Function<String, String> pipeline = op1.andThen(op2);
        System.out.println(pipeline.apply("abc"));

        Supplier<C1> o1 = C1::new;
        Function<Integer, C1> o2 = n -> new C1(n);
        C1 oneC1 = o1.get();
        C1 twoC1 = o2.apply(10);
    }

    class C1 {
        int a;
        C1(int a) {
            this.a = a;
        }

        C1() {
            a = 0;
        }
    }
}
