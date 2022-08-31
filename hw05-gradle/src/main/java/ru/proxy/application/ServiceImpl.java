package ru.proxy.application;

import ru.proxy.application.aop.Log;
import ru.proxy.application.aop.Service;

import java.util.Arrays;

@Log
public class ServiceImpl implements Service {


    @Override
    public void method1() {
        System.out.println("Method 1");
    }

    @Override
    public void method2() {
        System.out.println("Method 2");
    }

    @Override
    public void printHi(String name) {
        System.out.println("Hi, " + name);
    }

    @Override
    public void methodForInvoke(String... args) {
        System.out.println(Arrays.deepToString(args));
    }
}