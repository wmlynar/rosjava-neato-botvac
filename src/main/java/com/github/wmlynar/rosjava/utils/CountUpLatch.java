package com.github.wmlynar.rosjava.utils;

public class CountUpLatch {

    private int count = 0;
    private Object lock = new Object();

    public CountUpLatch(int number) {
        this.count = number;
    }

    public void countUp() {
        synchronized (lock) {
            count++;
            lock.notifyAll();
        }
    }

    public void awaitFor(int number) throws InterruptedException {
        synchronized (lock) {
            while (count < number) {
                lock.wait();
            }
        }
    }

    public int getCount() {
        synchronized (lock) {
            return count;
        }
    }

}
