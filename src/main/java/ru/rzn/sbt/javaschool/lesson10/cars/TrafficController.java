package ru.rzn.sbt.javaschool.lesson10.cars;

import java.util.concurrent.Semaphore;

public class TrafficController {

    private static Semaphore semaphore = new Semaphore(1,true);

    public void enterLeft() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void enterRight() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void leaveLeft() {
        semaphore.release();
    }

    public void leaveRight() {
        semaphore.release();
    }
}