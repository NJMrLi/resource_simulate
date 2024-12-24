package com.orange.simulate.system;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.util.Random;

import static com.orange.simulate.config.Config.TIME;

public class SimulateCPU {

    private static Random random = new Random();


    public static void lineGraph(double rate) {
        while (true) {
            CentralProcessor processor = new SystemInfo().getHardware().getProcessor();
            double cpuUsage = processor.getSystemCpuLoad();
            if (cpuUsage < rate) {
                doSomeSimpleWork(rate * TIME);

                try {
                    long sleep = (long) (TIME - rate * TIME);
                    if (sleep < 1) {
                        sleep = 1L;
                    }
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(Thread.currentThread().getName() + " CPU usage exceeds the limit : " + cpuUsage);
                try {
                    int randomNumber = 1 + random.nextInt(5);
                    Thread.sleep(TIME * randomNumber);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void doSomeSimpleWork(double time) {
        long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) < time) {
            // do nothing
        }
    }
}
