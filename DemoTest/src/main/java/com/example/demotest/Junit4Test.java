package com.example.demotest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Junit4Test {
    static String i=" ";

    @BeforeClass
    public static void Beforeclass(){
        i=i+"beforeclass ";
        System.out.println("beforeClass"+"i="+i);

    }
    @Before
    public void before(){
        i=i+"before ";
        System.out.println("before"+"i="+i);
    }
    @Test
    public void Test1(){
        i=i+"test1 ";
        System.out.println("Test1"+"i="+i);


    }
    @Test
    public void Test2(){
        i=i+"test2 ";
        System.out.println("test2"+"i="+i);
    }

    @After
    public void After(){
        i=i+"after ";
        System.out.println("after"+"i="+i);
    }
    @AfterClass
    public static void afterclass(){
        i=i+"afterclass ";
        System.out.println("afterClass"+"i="+i);
    }
}
