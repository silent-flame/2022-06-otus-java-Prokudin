package ru.otus.tests;

import ru.otus.After;
import ru.otus.Before;
import ru.otus.Test;

public class TestClass {

    @Before
    public void before(){

    }

    @Test
    public void run(){
        System.out.println("Run test");
    }

    @Test
    public void exception(){
        throw new IllegalArgumentException("Something went wrong");
    }

    @Test
    public void method(){

    }

    @After
    public void after(){

    }
}