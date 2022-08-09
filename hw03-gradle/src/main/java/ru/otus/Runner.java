package ru.otus;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class Runner {
    public static Set<Class> findAllClassesUsingClassLoader(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines().filter(line -> line.endsWith(".class")).map(line -> getClass(line, packageName)).collect(Collectors.toSet());
    }

    private static Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try {
            var totalTestCounter = 0;
            var failedTestCounter = 0;
            var classes = findAllClassesUsingClassLoader("ru.otus.tests");
            for (Class clazz : classes) {
                if (!clazz.isInterface()) {
                    var constructors = clazz.getConstructors();
                    if (constructors.length != 1) {
                        throw new IllegalArgumentException("Test class should have one public constructor");
                    }

                    var methodBefore = getMethod(clazz, Before.class);
                    var methodAfter = getMethod(clazz, After.class);

                    for (Method method : clazz.getMethods()) {
                        if (method.isAnnotationPresent(Test.class)) {
                            var testClassInstanse = constructors[0].newInstance(new String[]{});
                            try {
                                if (methodBefore != null) {
                                    methodBefore.invoke(testClassInstanse, new String[]{});
                                }
                                totalTestCounter++;
                                method.invoke(testClassInstanse, new String[]{});
                                System.out.printf("Test method {%s} passed \n", method.getName());
                            } catch (Exception e) {
                                System.err.printf("Excecution of test method {%s} failed \n", method.getName());
                                failedTestCounter++;
                            } finally {
                                if (methodAfter != null) {
                                    methodAfter.invoke(testClassInstanse, new String[]{});
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("Test methods found " + totalTestCounter);
            System.out.println("Passed tests " + (totalTestCounter - failedTestCounter));
            System.err.println("Failed tests " + failedTestCounter);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private static Method getMethod(Class clazz, Class annotation) {
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                return method;
            }
        }
        return null;
    }
}