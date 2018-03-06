import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Main {
    public static final int CARS_COUNT = 10;
    public static String winner = null;

    public static void main(String[] args) {
        final CountDownLatch startLatch = new CountDownLatch(CARS_COUNT);
        final CyclicBarrier startBarrier = new CyclicBarrier(CARS_COUNT + 1);
        final CountDownLatch finishLatch = new CountDownLatch(CARS_COUNT);
        final CountDownLatch winnerLatch = new CountDownLatch(1);
        Semaphore tunnelSemaphore = new Semaphore(CARS_COUNT / 2);

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(tunnelSemaphore), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10),
                    startLatch, startBarrier, finishLatch, winnerLatch);
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }
        try {
            startLatch.await();
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
            startBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        try {
            winnerLatch.await();
            System.out.println(winner + " - WIN");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            finishLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}



