package ru.rzn.sbt.javaschool.lesson10.balls;

import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.*;
import javax.swing.*;

/**
 * 1. Измените метода {@link Solution#main(String[])} таким образом, чтобы вместо явного создания потоков использовался
 * какой-нибудь {@link java.util.concurrent.Executor}.
 * 2. Реализуйте последовательную "заморозку" потоков при попадании {@link Ball} на диагональ {@link BallWorld}
 * (где x == y). Попаданием считать пересечение описывающего прямоуголькника диагонали. При заморозке всех потоков
 * осуществляйте возобновление выполнения
 * 3. Введите в программу дополнительный поток, который уничтожает {@link Ball} в случайные моменты времени.
 * Начните выполнение этого потока c задержкой в 15 секунд после старта всех {@link Ball}. {@link Ball} должны
 * уничтожаться в случайном порядке. Под уничтожением {@link Ball} подразумевается
 * а) исключение из массива {@link BallWorld#balls} (нужно реализовать потокобезопасный вариант)
 * б) завершение потока, в котором выполняется соответствующая задача (следует использовать
 * {@link java.util.concurrent.Future}сформированный при запуске потока для прерывания
 * {@link java.util.concurrent.Future#cancel(boolean)})
 */
public class Solution {

    public static void nap(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.err.println("Thread " + Thread.currentThread().getName() + " throwed exception " + e.getMessage());
        }
    }

    private static ArrayList initBalls(BallWorld world){
        ArrayList<Ball> ballArrayList = new ArrayList<>();
        ballArrayList.add(new Ball(world, 20, 200, 5, 10, Color.red));
        ballArrayList.add(new Ball(world, 70, 100, 8, 6, Color.blue));
        ballArrayList.add(new Ball(world, 150, 100, 9, 7, Color.green));
        ballArrayList.add(new Ball(world, 200, 130, 3, 8, Color.black));
                return ballArrayList;
    }

    public static void main(String[] a) {

        final BallWorld world = new BallWorld();
        final JFrame win = new JFrame();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                win.getContentPane().add(world);
                win.pack();
                win.setVisible(true);
            }
        });

        Thread.currentThread().setName("MyMainThread");

        ExecutorService executorService = Executors.newCachedThreadPool();
        ArrayList ballArrayList = initBalls(world);
        ArrayList <Future> ballFutureArrayList = new ArrayList<>();

        for (Object ball : ballArrayList) {
            ballFutureArrayList.add(executorService.submit((Runnable) ball));
            nap((int) (1000 * Math.random()));
        }


        /* * 3. Введите в программу дополнительный поток, который уничтожает {@link Ball} в случайные моменты времени.
         * Начните выполнение этого потока c задержкой в 15 секунд после старта всех {@link Ball}. {@link Ball} должны
         * уничтожаться в случайном порядке. Под уничтожением {@link Ball} подразумевается
         * а) исключение из массива {@link BallWorld#balls} (нужно реализовать потокобезопасный вариант)
         * б) завершение потока, в котором выполняется соответствующая задача (следует использовать
         * {@link java.util.concurrent.Future}сформированный при запуске потока для прерывания
         * {@link java.util.concurrent.Future#cancel(boolean)})*/
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(new Runnable() {
            @Override
            public void run() {
                int countBall;
                while (!ballFutureArrayList.isEmpty()){
                    countBall = (int) (Math.random() * ballFutureArrayList.size());
                    ballFutureArrayList.get(countBall).cancel(true);
                    ballFutureArrayList.remove(countBall);
                    nap((int) (5000 * Math.random()));
                }
            }
        },2,TimeUnit.SECONDS);

    }
}
