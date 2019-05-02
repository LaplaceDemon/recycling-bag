package io.github.laplacedemon.recyclingbag;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Test;

public class PointerPool {
    
    @Test
    public void testGetPointer() throws InterruptedException {
        RecyclingBag<Pointer> recyclingBag = new RecyclingBag<>(1024, 1024,()->{
            return new Pointer();
        });
        
        for(int i = 0;i<1024*2 + 1;i++) {
            Pointer pointer = recyclingBag.get();
            System.out.println(i + "," + pointer);
            recyclingBag.release(pointer);
        }
    }
    
    @Test
    public void testGetPointerMultithreading() throws InterruptedException {
        RecyclingBag<Pointer> recyclingBag = new RecyclingBag<>(1024, 1024,()->{
            return new Pointer();
        });
        
        for(int n = 0; n < 10; n++) {
        	new Thread(() -> {
            	for(int i = 0; i < 1024*2 + 1; i++) {
                    Pointer pointer = recyclingBag.get();
                    recyclingBag.release(pointer);
                }
            }, "thread-" + n).start();
        }
        
        Thread.sleep(1000*20);
    }
    
    @Test
    public void testJVMGetAndBack() {
        long size = 10000*10000*20L;
        long t0 = System.currentTimeMillis();
        for(long i = 0;i<size;i++) {
            new Pointer();
        }
        long t1 = System.currentTimeMillis();
        double dt = t1-t0;
        System.out.println(size*1000/dt);
    }
    
    @Test
    public void testLinkedListQueueJVMGetAndBack() {
        Queue<Pointer> queue = new LinkedList<Pointer>();
        for(int i = 0;i<1024;i++) {
            queue.add(new Pointer());
        }
        
        long size = 10000*10000*10L;
        long t0 = System.currentTimeMillis();
        for(long i = 0;i<size;i++) {
            Pointer ele = queue.poll();
            queue.add(ele);
        }
        long t1 = System.currentTimeMillis();
        double dt = t1-t0;
        System.out.println(size*1000/dt);
    }
    
    @Test
    public void testLocalArrayJVMGetAndBack() {
        ArrayQueue<Pointer> queue = new ArrayQueue<Pointer>(1024);
        for(int i = 0;i<1024;i++) {
            queue.add(new Pointer());
        }
        
        long size = 10000*10000*10L;
        
        long t0 = System.currentTimeMillis();
        for(long i = 0;i < size;i++) {
            Pointer ele = queue.take();
            queue.release(ele);
        }
        long t1 = System.currentTimeMillis();
        double dt = t1-t0;
        System.out.println(size*1000/dt);
    }
    
    @Test
    public void testGetAndBack() {
        RecyclingBag<Pointer> recyclingBag = new RecyclingBag<>(1024, 1024, ()->{
            return new Pointer();
        });
        
        long size = 10000*10000*10L;
        
        long t0 = System.currentTimeMillis();
        for(long i = 0;i<size;i++) {
            Pointer pointer = recyclingBag.get();
            recyclingBag.release(pointer);
        }
        long t1 = System.currentTimeMillis();
        double dt = t1-t0;
        System.out.println(size*1000/dt);
    }
}
