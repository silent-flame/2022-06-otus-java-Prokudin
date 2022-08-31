package ru.proxy.application;

import ru.proxy.application.aop.Log;
import ru.proxy.application.aop.Service;

import java.util.Arrays;

public class SuperService implements Service {

    @Override
    public void method1() {

    }

    @Override
    public void method2() {

    }

    @Override
    public void printHi(String name) {

    }

    @Log
    @Override
    public void methodForInvoke(String... args) {
        System.out.println(Arrays.deepToString(args));
    }
}