package com.github.wmlynar.rosjava.utils;

public class CountUpLatch {

    private int count = 0;
    private Object lock = new Object();

    public CountUpLatch() {
	}

    public CountUpLatch(int number) {
        this.count = number;
    }

    public void countUp() {
        synchronized (lock) {
            count++;
            lock.notifyAll();
        }
    }

    public int getCount() {
        synchronized (lock) {
            return count;
        }
    }

    public void awaitFor(int number) throws InterruptedException {
        synchronized (lock) {
            while (count < number) {
                lock.wait();
            }
        }
    }

    public int awaitFor(int number, long milliseconds) throws InterruptedException {
    	long expectedTime = System.currentTimeMillis() + milliseconds;
        synchronized (lock) {
            while (count < number && milliseconds > 0) {
                lock.wait(milliseconds);
                milliseconds = expectedTime - System.currentTimeMillis();
            }
            return count;
        }
    }

}
