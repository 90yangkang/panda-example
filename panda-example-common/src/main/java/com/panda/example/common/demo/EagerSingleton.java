package com.panda.example.common.demo;

/**
 * @author yk
 * @version 1.0
 * @describe 饿汉单例模式
 * @date 06-08 18:13
 */
public class EagerSingleton {
    private static final EagerSingleton eagerSingleton = new EagerSingleton();
    private EagerSingleton() {

    }
    public static EagerSingleton getInstance() {
        return eagerSingleton;
    }
}



