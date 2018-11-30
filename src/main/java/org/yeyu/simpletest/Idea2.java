package org.yeyu.simpletest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Idea2 {
    public static void main(String... args) {
        Idea2 idea = new Idea2();
        idea.test();
    }

    void test() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher("a").addJob("c1", "class1", "class2"));
        teachers.add(new Teacher("b").addJob("c1", "class3"));
        teachers.add(new Teacher("c").addJob("c1", "class4","class5", "class6"));

        teachers.stream().forEach(teacher -> {
            System.out.println(teacher.name);
        });

        System.out.println();
        //print teachers' name ordered by the number of classes they teach
//        Collections.sort(teachers, new Comparator<Teacher>() {
//            @Override
//            public int compare(Teacher t1, Teacher t2) {
//                return t1.task.get("c1").length - t2.task.get("c1").length;
//            }
//        });

        teachers.stream()
                .sorted((t1, t2) -> {return t1.getClassCount("c1") - t2.getClassCount("c1");})
                .forEach(t -> {
                    System.out.println(t.name);
                });

//        System.out.println();
//        teachers.stream().forEach(teacher -> {
//            System.out.println(teacher.name);
//        });
    }

    private class Teacher {
        private String name;
        private Map<String, String[]> task = new HashMap<>();
        Teacher(String _name) {
            name = _name;
        }

        Teacher addJob(String curriculum, String... classes) {
            task.put(curriculum, classes);
            return this;
        }

        int getClassCount(String curriculum) {
            String[] classes = task.get(curriculum);
            return classes == null ? 0 : classes.length;
        }
    }
}
