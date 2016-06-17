package test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import sun.nio.ch.DirectBuffer;

@SuppressWarnings("restriction")
public class ByteBufferTest {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) throws InterruptedException {
        System.out.println(System.getProperty("java.runtime.version"));
        System.out.println();
        System.out.println(_1MB);
        List<Object> list = new ArrayList<Object>();
        for (int i = 1; i <= 10; i++) {
            System.out.println("------------------ " + i + " ------------------");
            if (i > 5 && i % 2 == 0) {
                Object buffer = list.get(list.size() - 1);
                ((DirectBuffer) buffer).cleaner().clean();
                System.out.println("clean...");
            } else {
                list.add(ByteBuffer.allocateDirect(_1MB));
                System.out.println("add...");
            }
            // list.add(new byte[_1MB]);
            // list.add(ByteBuffer.wrap(new byte[_1MB]));
            printSimpleMemoryInfo();
            Thread.sleep(10 * 1000);
        }
        System.out.println("start sleep,list.size():" + list.size());
        Thread.sleep(120 * 1000);
        System.out.println(list.size());

        // java.nio.ByteBuffer.allocate(capacity) 
        // 使用 heap(java堆)分配内存:可以直接通过工具查看出内存占用
        
        // java.nio.ByteBuffer.allocateDirect(capacity) 
        // 使用non heap(非java堆)通过操作系统直接分配内存: 会导致查看不出java heap的内存占用，导致泄露看不出来
    }

    public static void printSimpleMemoryInfo() {
        Runtime runTime = Runtime.getRuntime();
        System.out.println("Total Mem:" + runTime.totalMemory() / 1024.0f + "KB, "
            + " Max Mem:" + runTime.maxMemory() / 1024.0f + "KB, "
            + " Free mem:" + runTime.freeMemory() / 1024.0f + "KB");
        
        printJvmMemory();
        System.out.println();
        printNioDirectMemoryInfo();
        System.out.println();
    }
    
    public static void printJvmMemory(){
        JvmMemory.print();
    }

    public static void printNioDirectMemoryInfo() {
        NioDirectMemory.print();
    }

    class MonitorInfoBean {
        /** 可使用内存. */
        private long totalMemory;
        /** 剩余内存. */
        private long freeMemory;
        /** 最大可使用内存. */
        private long maxMemory;
        /** 操作系统. */
        private String osName;
        /** 总的物理内存. */
        private long totalPhysicalMemorySize;
        /** 剩余的物理内存. */
        private long freePhysicalMemorySize;
        /** 已使用的物理内存. */
        private long usedMemory;
        /** 线程总数. */
        private int totalThread;
        /** cpu使用率. */
        private double cpuRatio;
        /* 生成get和set.... */
    }

}
