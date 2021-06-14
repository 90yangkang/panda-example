package com.panda.example.common.demo;

/**
 * @author yk
 * @version 1.0
 * @describe panda-example
 * @date 09-29 18:53
 */
public class DoubleCheckedSingleton {
    //被volatile修饰的变量的值，将不会被本地线程缓存，所有对该变量的读写都是直接操作共享内存，从而确保多个线程能正确的处理该变量。
    private volatile static DoubleCheckedSingleton instance = null;
    //私有构造方法
    private DoubleCheckedSingleton() {
    }
    //公共静态方法获取实例
    public static DoubleCheckedSingleton getSingletonInstance() {
        //先检查实例是否存在，不存在，在进行同步
        if (instance == null) {
            //同步块，线程安全的创建实例
            synchronized (DoubleCheckedSingleton.class) {
                //再次检查实例是否存在，如果不存在才真正的创建实例
                if (instance == null) {
                    instance = new DoubleCheckedSingleton();
                }
            }
        }
        return instance;
    }
}