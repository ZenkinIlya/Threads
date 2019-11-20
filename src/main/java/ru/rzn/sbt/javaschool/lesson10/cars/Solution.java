package ru.rzn.sbt.javaschool.lesson10.cars;

/**
 * Существует мост через реку с однополосным движением. Предполагается движение машин в обе стороны.
 * GUI предоставляет есть две очевидные кнопки для добавления машин слева и справа. Задачи разобраться в работе
 * графических компонент не стоит.
 * Требуется переписать класс {@link TrafficController} таким образом, чтобы синхронизовать выполнение задач {@link Car}
 * в разных потоках и избежать коллизий машин на мосту.
 */
public class Solution {

    private static void nap(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }

    public static void main(String[] a) {
        final CarWindow win = new CarWindow();

        win.pack();
        win.setVisible(true);

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    nap(25);
                    win.repaint();
                }
            }
        }).start();
    }

}
