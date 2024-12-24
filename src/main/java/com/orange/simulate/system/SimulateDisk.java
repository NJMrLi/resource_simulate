package com.orange.simulate.system;

import com.orange.simulate.config.Config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class SimulateDisk {

    private static final String fileName = "simulatefile.txt";

    public static void disk() {
        final double minUseRate = Config.DiskMinUseRate;
        final double maxUseRate = Config.DiskMaxUseRate;
        final String filePaths = Config.DiskFilePath;
        String[] split = filePaths.split(",");

        new Thread(() -> {
            while (true) {
                File totalFile = new File("/");
                long totalSpace = totalFile.getTotalSpace();
                long usableSpace = totalFile.getUsableSpace();
                long usedSpace = totalSpace - usableSpace;//已用大小
                double usedRate = (double) usedSpace / totalSpace;//已用占比
                System.out.println("根目录 总体已用占比:" + usedRate);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }).start();

        for (int i = 0; i < split.length; i++) {
            String filePath = split[i];
            new Thread(() -> {
                while (true) {
                    try {
                        // /home 占比
                        File totalFile = new File(filePath);
                        long totalSpace = totalFile.getTotalSpace();
                        long usableSpace = totalFile.getUsableSpace();
                        long usedSpace = totalSpace - usableSpace;//已用大小
                        double usedRate = (double) usedSpace / totalSpace;//已用占比
                        System.out.println(filePath + " 已用占比:" + usedRate);

                        Runtime process = Runtime.getRuntime();
                        String simulatefilePath = filePath + File.separator + fileName;

                        File file = new File(simulatefilePath);
                        long fileSize = 0;
                        if (file.exists()) {
                            fileSize = file.length();
                            System.out.println("文件大小:" + fileSize);
                        }

                        if (usedRate < minUseRate) {
                            //文件原大小+（最小占用阈值-已用大小）=新文件大小
                            long newFileSize = (long) Math.ceil((fileSize + (totalSpace * minUseRate - usedSpace)) / (1024 * 1024 * 1024));
                            System.out.println(simulatefilePath + "新文件大小:" + newFileSize);
                            String command = String.format("fallocate -l %sG %s", newFileSize, simulatefilePath);
                            System.out.println(simulatefilePath + "执行命令:" + command);
                            process.exec(command);
                        } else if (usedRate > maxUseRate && fileSize > 0) {
                            //最小占用阈值-（已用大小-文件原大小）=新文件大小
                            long newFileSize = (long) Math.ceil((totalSpace * minUseRate - (usedSpace - fileSize)) / (1024 * 1024 * 1024));
                            System.out.println(simulatefilePath + "新文件大小:" + newFileSize);
                            String command = String.format("truncate -s %sG %s", newFileSize, simulatefilePath);
                            System.out.println(simulatefilePath + "执行命令:" + command);
                            process.exec(command);
                        }
                        Thread.sleep(5000);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }

    }
}
