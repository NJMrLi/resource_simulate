package com.orange.simulate.system;

import com.orange.simulate.config.Args;
import com.orange.simulate.config.Config;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import static com.orange.simulate.config.Config.MB100;
import static com.orange.simulate.config.Config.TIME;

public class Simulate {

    public static void memory(int mb) throws InterruptedException {
        SimulateMemory.memory(mb);
    }

    public static void lineGraph(double rate) {
        SimulateCPU.lineGraph(rate);
    }

    public static void disk() {
        SimulateDisk.disk();
    }

    public static Args parseArgs(String[] args) {
        String rates = null, memory = null;
        //-c:60,60 -m:50
        if (args == null || args.length < 1) {
            //设置配置文件默认值
            rates = Config.UseCpuRate;
            memory = Config.UseMemory;
            // exit();
        }

        for (String param : args) {
            if (param.startsWith("-c:")) {

                param = param.substring(3, param.length());
                if (param != null && param.length() > 0) {
                    rates = param;
                }
            }
            if (param.startsWith("-m:")) {

                param = param.substring(3, param.length());
                if (param != null && param.length() > 0) {
                    memory = param;
                }
            }
        }

        if ((rates == null || rates.length() < 1) && (memory == null || memory.length() < 1)) {
            exit();
        }

        return new Args(rates, memory);
    }

    public static void exit() {
        System.out.println("Please enter parameters");
        System.out.println("such as: java xxxxx -c:80,40 -m:600");
        System.out.println("-C represents the control core, and - m represents the control memory");
        System.out.println("-c: 80 and 40 represent two cores, with utilization rates of 80% and 40% respectively");
        System.out.println("-m: 600 means about 600MB of memory (unit: megabyte)");
        System.exit(0);
    }
}
