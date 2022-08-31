package ru.proxy.application;

import ru.proxy.application.aop.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Instant;
import java.util.Arrays;

public class ProxyFactory {

    public <T> T createProxy(Object delegate, Class<T> interfacee) {
        return (T) Proxy.newProxyInstance(delegate.getClass().getClassLoader(), new Class[]{interfacee}, new DemoProxyHandler(delegate));
    }

    static class DemoProxyHandler implements InvocationHandler {
        private final Object delegate;

        DemoProxyHandler(Object delegate) {
            this.delegate = delegate;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method delegateMethod;
            if (args == null) {
                delegateMethod = delegate.getClass().getMethod(method.getName());
            } else {
                var agrTypes = (Class<?>[]) Arrays.stream(args).map(Object::getClass).toArray(Class[]::new);
                delegateMethod =
                        delegate.getClass().getMethod(method.getName(), agrTypes);
            }
            if (method.isAnnotationPresent(Log.class) || delegate.getClass().isAnnotationPresent(Log.class) || delegateMethod.isAnnotationPresent(Log.class)) {
                System.out.printf("%s || %s.%s args=%s %n", Instant.now(), delegate.getClass().getName(), method.getName(), Arrays.deepToString(args));
            }
            return method.invoke(delegate, args);
        }
    }
}