package com.rainbowsea.rabbitmq.utils;


/**
 * 睡眠工具类
 */
public class SleepUtils {
    public static void sleep(int second) {
        try {
            Thread.sleep(1000 * second);  // 单位毫秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


    }
}
