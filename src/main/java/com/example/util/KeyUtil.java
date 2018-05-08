package com.example.util;

import org.assertj.core.api.ThrowableAssert;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 生成订单号，不用担心冲突，因为数据库层面订单表主键唯一约束
 *
 * @author xuan
 * @create 2018-04-06 16:46
 **/
public class KeyUtil {
    public static String genOrderId() {
        return Long.toString(System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(90000) + 10000);
    }

    public static String genOrderId2(){
        Random random = new Random();
        return Long.toString(System.currentTimeMillis() + random.nextInt(90000) +10000);
    }

    public static void main(String[] args) {
        int count = 10000;
        CyclicBarrier barrier = new CyclicBarrier(count);
        long begin = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            new Thread(() -> {
                try {
                    barrier.await();
                    System.out.println(KeyUtil.genOrderId2());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        while (Thread.activeCount() > 1){
            Thread.yield();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - begin);
    }
}
