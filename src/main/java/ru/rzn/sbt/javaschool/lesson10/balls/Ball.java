package ru.rzn.sbt.javaschool.lesson10.balls;

import java.awt.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Ball implements Runnable {

    BallWorld world;

    private volatile boolean visible = false;

    private int xpos, ypos, xinc, yinc;

    private final Color col;

    private final static int BALLW = 10;
    private final static int BALLH = 10;

    public Ball(BallWorld world, int xpos, int ypos, int xinc, int yinc, Color col) {

        this.world = world;
        this.xpos = xpos;
        this.ypos = ypos;
        this.xinc = xinc;
        this.yinc = yinc;
        this.col = col;

        world.addBall(this);
    }

    public void deleteBall(){
        world.removeBall(this);
    }

    @Override
    public void run() {
        this.visible = true;
        try {
            while (true) {
                move();
            }
        } catch (InterruptedException e ){
            world.removeBall(this);
        }
    }

    private static final CyclicBarrier barrier = new CyclicBarrier(4, new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    private boolean coincidence(){
        int currentXPos;
        for (int i = -BALLW; i <= BALLW; i++){
            currentXPos = xpos + i;
            if (currentXPos == ypos && xpos > ypos){
                return true;
            }
        }
        return false;
    }

    public void move() throws InterruptedException {
        if (xpos >= world.getWidth() - BALLW || xpos <= 0) xinc = -xinc;

        if (ypos >= world.getHeight() - BALLH || ypos <= 0) yinc = -yinc;

        Thread.sleep(30);
        doMove();
        world.repaint();
        if (coincidence()){
            try {
                barrier.await();
            } catch (BrokenBarrierException e) {
                System.out.println("BrokenBarrierException");
            }
        }
    }

    public synchronized void doMove() {
        xpos += xinc;
        ypos += yinc;
    }

    public synchronized void draw(Graphics g) {
        if (visible) {
            g.setColor(col);
            g.fillOval(xpos, ypos, BALLW, BALLH);
        }
    }
}
