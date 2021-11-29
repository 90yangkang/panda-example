package com.example.pandaexampleshardingjdbc;


/**
 * @Author : kang.yang
 * @Date: 2020-08-26 16:22
 * @Version : 1.0
 * @Description :存储/获取当前线程的用户信息
 */
public abstract class TradeUtils {

    //线程变量，存放user实体类信息，即使是静态的与其他线程也是隔离的
    private static final ThreadLocal<String> userThreadLocal = new ThreadLocal<String>();

    //从当前线程变量中获取用户信息
    public static String getLoginUser() {
        String user = userThreadLocal.get();
        return user;
    }


    //为当前的线程变量赋值上用户信息
    public static void setLoginUser(String user) {
        userThreadLocal.set(user);
    }

    //清除线程变量
    public static void removeUser() {
        userThreadLocal.remove();
    }

}
