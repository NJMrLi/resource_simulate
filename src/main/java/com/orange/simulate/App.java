package com.orange.simulate;

import com.orange.simulate.config.Args;
import com.orange.simulate.config.Config;
import com.orange.simulate.system.Simulate;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import java.io.File;


import static com.orange.simulate.system.Simulate.parseArgs;
import static com.orange.simulate.system.SystemMonitor.printInfo;

public class App {
    public static void main(String[] args) {

        System.out.println("Program is starting...");

        //定时打印目前CPU、内存、磁盘
        printInfo();

        Args result = parseArgs(args);
        System.out.println("CPU params " + result.getRates());
        System.out.println("Memory params " + result.getMemory());

        final String filePaths = Config.DiskFilePath;
        String[] split = filePaths.split(",");
        for (String filePath : split) {
            File totalFile = new File(filePath);
            long totalSpace = totalFile.getTotalSpace();
            long usableSpace = totalFile.getUsableSpace();
            long usedSpace = totalSpace - usableSpace;//已用大小
            double usedRate = (double) usedSpace / totalSpace;//已用占比
            System.out.println(filePath + " 已用占比:" + usedRate);
        }

        // 获取Runtime对象
        Runtime runtime = Runtime.getRuntime();
        // 获取最大堆内存大小（单位为字节）
        long maxMemory = runtime.maxMemory();
        // 将字节转换为MB（如果需要）
        double maxMemoryInMB = (double) maxMemory / (1024 * 1024);
        System.out.println("JVM最大堆内存设置（-Xmx）: " + maxMemoryInMB + " MB");

        if(!Config.OnlyPrint){
            //CPU
            if (Config.EnableCPU) {
                System.out.println("simulate CPU start.... ");
                if (result.getRates() != null && result.getRates().length() > 0) {
                    SystemInfo si = new SystemInfo();
                    CentralProcessor processor = si.getHardware().getProcessor();
                    //采用逻辑核心
                    int logicalProcessorCount = (int) (processor.getLogicalProcessorCount() * Config.UseCPULogicCoreRate);

                    final String[] rateArr = result.getRates().split(",");
                    if(rateArr.length==1){
                        System.out.println("开始执行 总线程数 ：" + logicalProcessorCount);
                        for (int i = 0; i < logicalProcessorCount; i++) {
                            new Thread(() -> {
                                double r = Double.valueOf(rateArr[0]) / 100;
                                Simulate.lineGraph(r);
                            }).start();
                        }
                    }else {
                        for (String rate : rateArr) {
                            new Thread(() -> {
                                double r = Double.valueOf(rate) / 100;
                                Simulate.lineGraph(r);
                            }).start();
                        }
                    }
                }
            }
            //内存
            if (Config.EnableMemory) {
                System.out.println("simulate Memory start.... ");
                if (result.getMemory() != null && result.getMemory().length() > 0) {
                    Integer finalMemory = Integer.valueOf(result.getMemory());
                    new Thread(() -> {
                        try {
                            Simulate.memory(finalMemory);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
            //磁盘处理
            if (Config.EnableDisk) {
                System.out.println("simulate DISK start.... ");
                Simulate.disk();
            }

        }

        System.out.println("Program is started...");

        //程序关闭做一些处理
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Program is shutdown...");
        }));
    }

}
