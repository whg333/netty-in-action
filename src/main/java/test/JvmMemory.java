package test;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import com.sun.management.OperatingSystemMXBean;

@SuppressWarnings("restriction")
public class JvmMemory {
    
    public static final JvmMemory instance = new JvmMemory();
    
    private final OperatingSystemMXBean osmb;
    private final MemoryUsage heapMemoryUsage;
    private final MemoryUsage nonHeapMemoryUsage;
    
    public JvmMemory() {
        osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        MemoryMXBean mmb = (MemoryMXBean) ManagementFactory.getMemoryMXBean();
        heapMemoryUsage = mmb.getHeapMemoryUsage();
        nonHeapMemoryUsage = mmb.getNonHeapMemoryUsage();
    }
    
    public long getTotalPhysicalMemory(){
        return osmb.getTotalPhysicalMemorySize();
    }
    
    public long getFreePhysicalMemory(){
        return osmb.getFreePhysicalMemorySize();
    }
    
    public long getInitHeapMemory(){
        return heapMemoryUsage.getInit();
    }
    
    public long getUsedHeapMemory(){
        return heapMemoryUsage.getUsed();
    }
    
    public long getMaxHeapMemory(){
        return heapMemoryUsage.getMax();
    }
    
    public long getInitNonHeapMemory(){
        return nonHeapMemoryUsage.getInit();
    }
    
    public long getUsedNonHeapMemory(){
        return nonHeapMemoryUsage.getUsed();
    }
    
    public long getMaxNonHeapMemory(){
        long max = nonHeapMemoryUsage.getMax();
        return max == -1 ? 0 :nonHeapMemoryUsage.getMax();
    }
    
    public static void print() {
        System.out.println("System Total Physical Memory=" + JvmMemory.instance.getTotalPhysicalMemory() / 1024.0f + "KB");
        System.out.println("System Free Physical Memory=" + JvmMemory.instance.getFreePhysicalMemory() / 1024.0f + "KB");

        System.out.println("\nJvm Heap Memory:");
        System.out.println("Init Memory=" + JvmMemory.instance.getInitHeapMemory() / 1024.0f + "KB");
        System.out.println("Used Memory=" + JvmMemory.instance.getUsedHeapMemory() / 1024.0f + "KB");
        System.out.println("Can Used Max Memory=" + JvmMemory.instance.getMaxHeapMemory() / 1024.0f + "KB");
        
        System.out.println("\nJvm NonHeap Memory:");
        System.out.println("Init Memory=" + JvmMemory.instance.getInitNonHeapMemory() / 1024.0f + "KB");
        System.out.println("Used Memory=" + JvmMemory.instance.getUsedNonHeapMemory() / 1024.0f + "KB");
        System.out.println("Can Used Max Memory=" + JvmMemory.instance.getMaxNonHeapMemory() / 1024.0f + "KB");
    }
    
}
