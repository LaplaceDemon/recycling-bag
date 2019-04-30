package io.github.laplacedemon.recyclingbag;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Supplier;

public class RecyclingBag<T> {
    private ArrayBlockingQueue<T> pool = new ArrayBlockingQueue<T>(1024);
    private ThreadLocal<ArrayQueue<T>> localVar;
    private Supplier<? extends T> supplier;
    
    public RecyclingBag(Supplier<? extends T> supplier) {
        this.supplier = supplier;
        this.localVar = ThreadLocal.withInitial(()->{
            final ArrayQueue<T> queue = new ArrayQueue<T>(1024);
            
            for(int i = 0;i<1024;i++) {
                T x = supplier.get();
                queue.add(x);
            }
            System.out.println("返回queue");
            return queue;
        });
        
        for(int i = 0; i < 1024; i++) {
            T x = supplier.get();
            this.pool.add(x);
        }
    }
    
    public T get() {
        T x = this.localVar.get().take();
        if(x != null) {
            return x;
        }
        
        x = this.pool.poll();
        if(x != null) {
            System.out.println("从公共池中获取");
            return x;
        }
        System.out.println("JVM获取");
        return supplier.get();
    }
    
    public void release(T obj) {
        ArrayQueue<T> queue = this.localVar.get();
        if(queue.size() >= 1024) {
            System.out.println("归还到公共池");
            this.pool.add(obj);
            return ;
        }
        queue.add(obj);
    }
    
}
