package com.dchealth.web.job.form;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Auther: cuitao
 * @Date: 2023/6/8 - 06 - 08 - 9:07
 * @Description: com.dchealth.web.job.form
 */
public class Demo {
    private static CyclicBarrier barrier = new CyclicBarrier(3, () -> {
        System.out.println("等到各位大佬都到位之后，分发护照和签证等内容！");
    });

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(6);


        System.out.println("主线程执行1 = " + "主线程执行1");
        executor.execute(Demo::m_a);
        Thread.sleep(100);
        executor.execute(Demo::m_b);
        Thread.sleep(100);
        executor.execute(Demo::m_c);
        executor.shutdown();
        System.out.println("主线程执行2 = " + "主线程执行2");

//
//            m_d(barrier);
//
//        Thread.sleep(100);
//
//            m_e(barrier);
//
//        Thread.sleep(100);
//
//        if (m_f(barrier)) return;



    }


    private static boolean m_f(CyclicBarrier barrier) {
        System.out.println("3到位！！！");
        try {
            barrier.await();
        } catch (Exception e) {
            System.out.println("悲剧，人没到齐！");
            return true;
        }
        System.out.println("3出发！！！");
        return false;
    }

    private static void m_e(CyclicBarrier barrier) {
        System.out.println("2到位！！！");
        try {
            barrier.await();
        } catch (Exception e) {
            System.out.println("悲剧，人没到齐！");
            return;
        }
        System.out.println("2出发！！！");
    }

    private static void m_d(CyclicBarrier barrier) {
        System.out.println("1到位！！！");
        try {
            barrier.await();
        } catch (Exception e) {
            System.out.println("悲剧，人没到齐！");
            return;
        }
        System.out.println("1出发！！！");
    }

    private static void m_c() {
        System.out.println("Rose到位！！！");
        try {
            barrier.await();
        } catch (Exception e) {
            System.out.println("悲剧，人没到齐！");
            return;
        }
        System.out.println("Rose出发！！！");
    }

    private static void m_b() {
        System.out.println("Jack到位！！！");
        try {
            barrier.await();
        } catch (Exception e) {
            System.out.println("悲剧，人没到齐！");
            return;
        }
        System.out.println("Jack出发！！！");
    }

    private static void m_a() {
        System.out.println("Tom到位！！！");
        try {
            barrier.await();
        } catch (Exception e) {
            System.out.println("悲剧，人没到齐！");
            return;
        }
        System.out.println("Tom出发！！！");
    }


}
