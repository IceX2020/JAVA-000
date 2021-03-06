package java0.conc0303;

import java.util.concurrent.CyclicBarrier;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * 一个简单的代码参考：
 */
public class Homework03_02 {
    private static int result;
    private volatile static int flag = 0;

    public static void main(String[] args)  throws InterruptedException{
        long start=System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        CyclicBarrier cyclicBarrier = new CyclicBarrier(4, new Runnable() {
            @Override
            public void run() {
                flag = 1;
                System.out.println("异步计算进程：" + Thread.currentThread().getName());
            }
        });
        for (int i = 0; i < 4; i++) {
            new Thread(new Homework03_02.readNum(i,cyclicBarrier)).start();
        }

        while(flag == 0) {
//            Thread.sleep(1);  flag 需要设置volatile，否则需要设置sleep，不然老是死循环
        }
        System.out.println("异步计算结果为：" + result);
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        // 然后退出main线程
    }
    
    private static int sum() {
        return fibo(36);
    }
    
    private static int fibo(int a) {
        if ( a < 2) 
            return 1;
        return fibo(a-1) + fibo(a-2);
    }

    static class readNum implements Runnable{
        private int id;
        private CyclicBarrier cyc;
        public readNum(int id, CyclicBarrier cyc){
            this.id = id;
            this.cyc = cyc;
        }
        @Override
        public void run() {
            synchronized (this){
                System.out.println("id:"+id+","+Thread.currentThread().getName());
                try {
                    //cyc.await();
                    result = sum();
                    cyc.await();   // 注意跟CountDownLatch不同，这里在子线程await
                    System.out.println("线程组任务" + id + "结束，其他任务继续");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
