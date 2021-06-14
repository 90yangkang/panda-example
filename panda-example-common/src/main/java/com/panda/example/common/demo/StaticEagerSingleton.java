package com.panda.example.common.demo;

/**
 * @author yk
 * @version 1.0
 * @describe 静态块饿汉单例模式
 * @date 06-08 18:13
 */
public class StaticEagerSingleton {

    private static final StaticEagerSingleton staticEagerSingleton;

    static {
        staticEagerSingleton = new StaticEagerSingleton();
    }

    private StaticEagerSingleton() {

    }

    public static StaticEagerSingleton getInstance() {
        return staticEagerSingleton;
    }
}