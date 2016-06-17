package test;

import java.lang.reflect.Field;

public class NioDirectMemory {

    public static final NioDirectMemory instance = new NioDirectMemory();
    
    private Field reservedMemory;
    private Field maxMemory;
    
    private NioDirectMemory(){
        try {
            Class<?> c = Class.forName("java.nio.Bits");
            maxMemory = c.getDeclaredField("maxMemory");
            maxMemory.setAccessible(true);
            reservedMemory = c.getDeclaredField("reservedMemory");
            reservedMemory.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void print(){
        System.out.println("NioDirectMemory Max Memory="+instance.getMaxMemoryBytes()/1024.0f + "KB");
        System.out.println("NioDirectMemory Used Memory="+instance.getUsedMemoryBytes()/1024.0f + "KB");
    }
    
    public long getMaxMemoryBytes(){
        try {
            return (Long)maxMemory.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public long getUsedMemoryBytes(){
        try {
            return (Long)reservedMemory.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
}
