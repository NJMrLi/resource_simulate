package com.orange.simulate.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.ResourceBundle;

public class Config {
    public static Integer TIME;
    public static Integer MB100;
    public static String UseCpuRate;
    public static Float UseCPULogicCoreRate;
    public static String UseMemory;
    public static Float UseMemoryRate;
    public static Float DiskMinUseRate;
    public static Float DiskMaxUseRate;
    public static String DiskFilePath;
    public static Boolean EnableCPU;
    public static Boolean EnableMemory;
    public static Boolean EnableDisk;
    public static Integer MonitorInterval;
    public static Boolean OnlyPrint;

    static {
        TIME = 1000; //直接设置为1s
        MB100 = 104857600;
        String configFilePath = System.getProperty("config.file");
        if (configFilePath != null) {
            Path configPath = Paths.get(configFilePath);
            try (InputStream inputStream = Files.newInputStream(configPath)) {
                // 读取配置文件
                // 加载配置文件
                Properties props = new Properties();
                props.load(inputStream);
                // 读取配置项
                EnableCPU = Boolean.valueOf(props.getProperty("EnableCPU"));
                EnableMemory = Boolean.valueOf(props.getProperty("EnableMemory"));
                EnableDisk = Boolean.valueOf(props.getProperty("EnableDisk"));
                UseCpuRate = props.getProperty("UseCpuRate");
                UseMemory = props.getProperty("UseMemory");
                DiskMinUseRate = Float.valueOf(props.getProperty("DiskMinUseRate"));
                DiskMaxUseRate = Float.valueOf(props.getProperty("DiskMaxUseRate"));
                DiskFilePath = props.getProperty("DiskFilePath");
                MonitorInterval = Integer.parseInt(props.getProperty("MonitorInterval"));
                OnlyPrint = Boolean.valueOf(props.getProperty("OnlyPrint"));
                UseMemoryRate = Float.valueOf(props.getProperty("UseMemoryRate"));
                UseCPULogicCoreRate = Float.valueOf(props.getProperty("UseCPULogicCoreRate"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ResourceBundle bundle = ResourceBundle.getBundle("config");
            UseCpuRate = bundle.getString("UseCpuRate");
            UseMemory = bundle.getString("UseMemory");
            DiskMinUseRate = Float.valueOf(bundle.getString("DiskMinUseRate"));
            DiskMaxUseRate = Float.valueOf(bundle.getString("DiskMaxUseRate"));
            DiskFilePath = bundle.getString("DiskFilePath");
            EnableCPU = Boolean.valueOf(bundle.getString("EnableCPU"));
            EnableMemory = Boolean.valueOf(bundle.getString("EnableMemory"));
            EnableDisk = Boolean.valueOf(bundle.getString("EnableDisk"));
            MonitorInterval = Integer.valueOf(bundle.getString("MonitorInterval"));
            OnlyPrint = Boolean.valueOf(bundle.getString("OnlyPrint"));
            UseMemoryRate = Float.valueOf(bundle.getString("UseMemoryRate"));
            UseCPULogicCoreRate = Float.valueOf(bundle.getString("UseCPULogicCoreRate"));
        }


    }
}