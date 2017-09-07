package com.github.wmlynar.rosjava.utils;

public class CountUpLatch {

    private int count = 0;
    private Object lock = new Object();

    public CountUpLatch() {
	}

    public CountUpLatch(int number) {
        this.count = number;
    }

	public void setCount(int i) {
        synchronized (lock) {
            count=i;
            lock.notifyAll();
        }
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
    	long wait = milliseconds;
    	long expectedTime = System.currentTimeMillis() + wait;
        synchronized (lock) {
            while (count < number && wait > 0) {
                lock.wait(wait);
                wait = expectedTime - System.currentTimeMillis();
            }
            return count;
        }
    }


}
