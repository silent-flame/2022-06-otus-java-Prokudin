package ru.proxy.application;

import ru.proxy.application.aop.Service;

public class ProxyDemo {

    public static void main(String[] args) {
        var factory = new ProxyFactory();

        var service = factory.createProxy(new ServiceImpl(), Service.class);
        service.method1();
        service.method2();
        service.printHi("Mikky");

        var superService = factory.createProxy(new SuperService(), Service.class);
        superService.methodForInvoke("Arg 1", "Arg 2");
    }
}