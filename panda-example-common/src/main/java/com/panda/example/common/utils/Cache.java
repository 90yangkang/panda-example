package com.panda.example.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author yk
 * @version 1.0
 * @describe panda-example
 * @date  2017-09-19 11:04:23
 */
public class Cache {
    static Map<String, Object> sourceDataMap = new HashMap<>();
    static ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();
    static Lock rLock = rwlock.readLock();
    static Lock wLock = rwlock.writeLock();
    //读取数据
    public static final Object get(String key) {
        rLock.lock();
        try {
            return sourceDataMap.get(key);
        } finally {
            rLock.unlock();
        }
    }
    //写入数据
    public static final Object add(String key, Object objValue) {
        wLock.lock();
        try {
            return sourceDataMap.put(key, objValue);
        } finally {
            wLock.unlock();
        }
    }
    //清空数据
    public static final void empty() {
        wLock.lock();
        try {
            sourceDataMap.clear();
        } finally {
            wLock.unlock();
        }
    }
}
