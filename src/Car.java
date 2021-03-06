import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {
    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;
    }
    private Race race;
    private int speed;
    private String name;
    private CountDownLatch startLatch;
    private CyclicBarrier startBarrier;
    private CountDownLatch finishLatch;
    private CountDownLatch winnerLatch;

    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed,
               CountDownLatch startLatch, CyclicBarrier startBarrier, CountDownLatch finishLatch,
               CountDownLatch winnerLatch) {
        this.race = race;
        this.speed = speed;
        this.startLatch = startLatch;
        this.startBarrier = startBarrier;
        this.finishLatch = finishLatch;
        this.winnerLatch = winnerLatch;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            startLatch.countDown();
            startBarrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        finishLatch.countDown();
        Main.winner = name;
        winnerLatch.countDown();
    }
}