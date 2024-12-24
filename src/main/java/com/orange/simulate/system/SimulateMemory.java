package com.orange.simulate.system;

import com.orange.simulate.config.Config;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.Vector;

import static com.orange.simulate.config.Config.MB100;

public class SimulateMemory {

    private static double random() {
        return (7 + Math.random() * (3)) / 10;
    }

    @SuppressWarnings("unchecked")
    public static void memory(int mb) throws InterruptedException {
        //占用率优先
        if (Config.UseMemoryRate > 0 && Config.UseMemoryRate < 1) {
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();
            GlobalMemory memory = hal.getMemory();

            //死循环
            while (true) {
                Thread.sleep(2000);
                long totalSize = memory.getTotal();
                long usedSize = totalSize - memory.getAvailable();
                float rate = (float) usedSize / totalSize;
                System.out.println("内存使用率： " + rate + " 内存限制：" + Config.UseMemoryRate);
                Vector v = new Vector();
                long exceptMemory = (long) (totalSize * Config.UseMemoryRate);
                if (rate < Config.UseMemoryRate) {
                    long needMemory = (exceptMemory - usedSize) / (1024 * 1024);
                    System.out.println("内存未超限 增加内存：" + needMemory + "MB");
                    int count = (int) needMemory / 100;
                    System.out.println("count" + count + "MB");
                    for (int i = 0; i < count; i++) {
                        int size = (int) (MB100 * random());
                        byte b1[] = new byte[size]; // 100M
                        v.add(b1);
                    }
                }
                //如果内存占用以及超过了，可以尝试remove
                if (rate > Config.UseMemoryRate) {
                    System.out.println("内存超限制,清理内存");
                    try {
                        Thread.sleep(1000);
                        System.gc();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (v.size() > 0) {
                        v.remove(0);
                    }
                }
            }
        } else {
            //固定占用优先级低
            System.out.println("内存固定大小模式");
            @SuppressWarnings("rawtypes")
            Vector v = new Vector();
            long needMemory = 0;
            System.out.println("内存未超限 增加内存：" + needMemory + "MB");
            for (int i = 0; i < ((int) mb / 100); i++) {
                int size = (int) (MB100 * random());
                byte b1[] = new byte[size]; // 100M
                v.add(b1);
                needMemory = needMemory + size;
            }
            needMemory = needMemory / (1024 * 1024);
            System.out.println("实际增加内存：" + needMemory + "MB");

            while (true) {
                try {
                    Thread.sleep(1000);
                    System.gc();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (v.size() > 0) {
                    v.remove(0);
                    int size = (int) (MB100 * random());
                    byte b1[] = new byte[size]; // 100M
                    v.add(b1);
                }
            }
        }
    }
}
