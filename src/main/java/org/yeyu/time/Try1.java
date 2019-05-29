package org.yeyu.time;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class Try1 {
    public static void main(String... a) {
        d1();
    }
    static void d1() {
//        LocalDate endDate = LocalDate.parse("2022-05-30");
//        System.out.println(endDate.compareTo(LocalDate.now()) < 0);
//
//        Period p = Period.between(LocalDate.now(), endDate);
//        System.out.println(String.format("remains %d year %d month %d day", p.getYears(), p.getMonths(), p.getDays()));

        LocalDateTime oneDT = LocalDateTime.parse("2019-04-12T15:51:00");
        oneDT = LocalDateTime.ofEpochSecond(1555055497, 0, ZoneOffset.ofHours(8));
        oneDT = oneDT.plusDays(24).plusHours(1);
        System.out.println(oneDT);

        LocalDateTime nowDT = LocalDateTime.now();
        System.out.println(nowDT);

        Duration d = Duration.between(oneDT, nowDT);
        System.out.println(String.format("gap %d sec", d.getSeconds()));
    }
}
