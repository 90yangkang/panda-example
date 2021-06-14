package com.panda.example.common.demo;

/**
 * @author yk
 * @version 1.0
 * @describe 懒汉式，需要的时候才创建
 * @date 06-08 18:19
 */
public class LazySingleton {
    //静态属性用来缓存创建实例
    private static LazySingleton instance = null;
    //私有构造方法避免程序自由创建实例
    private LazySingleton() {
    }
    //静态公共方法用于取得该类实例
    public static synchronized LazySingleton getLazySingletonInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}
