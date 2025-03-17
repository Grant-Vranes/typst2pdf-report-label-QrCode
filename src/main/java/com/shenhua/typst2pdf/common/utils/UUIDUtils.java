package com.shenhua.typst2pdf.common.utils;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class UUIDUtils {
    private static final ReentrantLock lock = new ReentrantLock();

    public static String uuid() {
        lock.lock();
        try {
            Long time = System.currentTimeMillis();
            Random random = new Random(100);
            String all = time + "" + random.longs(time);
            return UUID.nameUUIDFromBytes(all.getBytes()).toString().replace("-", "");
        } finally {
            lock.unlock();
        }
    }

    // public static String uuid() {
    //     return UUID.randomUUID().toString().toLowerCase().replaceAll("-", "");
    // }

}
