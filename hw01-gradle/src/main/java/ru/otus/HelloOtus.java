package ru.otus;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class HelloOtus {
    public static void main(String... args) {
        List<Integer> example = new ArrayList<>();
        int min = 0;
        int max = 20;
        for (int i = min; i < max; i++) {
            example.add(i);
        }

        System.out.println(Lists.partition(example, 5));
    }
}