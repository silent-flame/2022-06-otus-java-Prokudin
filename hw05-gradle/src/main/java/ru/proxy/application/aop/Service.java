package ru.proxy.application.aop;

public interface Service {
    void methodForInvoke(String... args);

    void method1();

    void method2();

    void printHi(String name);
}
