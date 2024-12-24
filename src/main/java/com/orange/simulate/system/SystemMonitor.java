package com.orange.simulate.system;

import com.orange.simulate.config.Config;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.FormatUtil;
import oshi.util.Util;

import java.io.File;


public class SystemMonitor {

    public static void printInfo() {
        Thread consumerThread = new Thread(() -> {
            testSystemUsage();
        });
        consumerThread.start();
    }

    private static void testSystemUsage() {
        while (true) {
            Util.sleep(Config.MonitorInterval);
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();
            CentralProcessor processor = hal.getProcessor();

            GlobalMemory memory = hal.getMemory();
            if (Config.OnlyPrint) {
                printMemory(memory);
                printCpu(processor);
                printDisks();
                System.out.println();
            } else {
                if (Config.EnableMemory) {
                    printMemory(memory);
                }
                if (Config.EnableCPU) {
                    printCpu(processor);
                }
                if (Config.EnableDisk) {
                    printDisks();
                }
                System.out.println();
            }
        }
    }

    private static void printMemory(GlobalMemory memory) {
        System.out.println("===================================================");
        long totalSize = memory.getTotal();
        String total = FormatUtil.formatBytes(totalSize);
        long usedSize = totalSize - memory.getAvailable();
        String used = FormatUtil.formatBytes(usedSize);
        System.out.println("内存总大小：" + total + " 已使用内存：" + used + " 使用率：" + (float) usedSize / totalSize * 100 + "%");
        System.out.println();
    }

    private static void printCpu(CentralProcessor processor) {

        // Wait a second...
        System.out.println("===================================================");
        int logicalProcessorCount = processor.getLogicalProcessorCount();
        int physicalProcessorCount = processor.getPhysicalProcessorCount();
        System.out.println("逻辑核心数量 logicalProcessorCount:" + logicalProcessorCount);
        System.out.println("物理核心数量 physicalProcessorCount:" + physicalProcessorCount);
        System.out.format("CPU load: %.1f%% (counting ticks)%n", processor.getSystemCpuLoadBetweenTicks() * 100);
        System.out.format("CPU load: %.1f%% (OS MXBean)%n", processor.getSystemCpuLoad() * 100);
        double[] loadAverage = processor.getSystemLoadAverage(3);
        System.out.println("CPU load averages:");
        System.out.println("1分钟系统负载平均值"  + (  loadAverage[0] < 0 ? " N/A" : String.format(" %.2f", loadAverage[0])));
        System.out.println("5分钟系统负载平均值"  + (  loadAverage[1] < 0 ? " N/A" : String.format(" %.2f", loadAverage[1])));
        System.out.println("15分钟系统负载平均值"  + (  loadAverage[2] < 0 ? " N/A" : String.format(" %.2f", loadAverage[2])));
        // per core CPU
        StringBuilder procCpu = new StringBuilder("CPU load per processor:");
        double[] load = processor.getProcessorCpuLoadBetweenTicks();
        for (double avg : load) {
            procCpu.append(String.format(" %.1f%% ", avg * 100));
        }
        System.out.println(procCpu);
        System.out.println("");

    }

    private static void printDisks() {
        System.out.println("===================================================");
        File[] disks = File.listRoots();
        for (File file : disks) {
            System.out.print(file.getPath() + " ");
            String total = String.format(" 总容量: %.2f GB", (double) file.getTotalSpace() / 1073741824);
            System.out.print(total);// 总空间
            String free = String.format(" 未使用: %.2f GB", (double) file.getFreeSpace() / 1073741824);
            System.out.print(free);// 总空间
            System.out.print(" 使用率 = " + (float) (file.getTotalSpace() - file.getFreeSpace()) / file.getTotalSpace() * 100 + "%");// 总空间
            System.out.println();
        }

        final String filePaths = Config.DiskFilePath;
        String[] split = filePaths.split(",");
        System.out.println("配置路径所在挂载点使用占比：");// 总空间
        for (String filePath : split) {
            File totalFile = new File(filePath);
            long totalSpace = totalFile.getTotalSpace();
            long usableSpace = totalFile.getUsableSpace();
            long usedSpace = totalSpace - usableSpace;//已用大小
            double usedRate = (double) usedSpace / totalSpace;//已用占比
            System.out.println(filePath + " 已用占比:" + usedRate);
        }
        System.out.println();
    }

}
