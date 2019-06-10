package com.integra.demo_app;

public class OddEvenmain {
    boolean odd;
    int count = 1;
    int max = 20;

    public void oddmethod() {
        synchronized (this) {
            while (count < max) {
                System.out.println("checking for odd thread" + count);

                while (!odd) {
                    try {
                        System.out.println("odd waiting thread" + count);
                        wait();
                        System.out.println("notified odd thread" + count);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("odd thread " + count);
                    count++;
                    odd = false;
                    notify();

                }
            }
        }

    }

    public void evenmethod() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (this) {
            while (count < max) {
                System.out.println("coming to even thread" + count);
                while (odd) {
                    System.out.println("even waiting thread" + count);
                    try {
                        wait();
                        System.out.println("even noticed thread" + count);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("even thread" + count);
                    count++;
                    odd = true;
                    notify();

                }
            }
        }
    }


}
