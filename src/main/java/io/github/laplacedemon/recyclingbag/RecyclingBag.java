package io.github.laplacedemon.recyclingbag;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Supplier;

public class RecyclingBag<T> {
    private ArrayBlockingQueue<T> publicPool = new ArrayBlockingQueue<T>(1024);
    private ThreadLocal<ArrayQueue<T>> localVar;
    private Supplier<? extends T> supplier;
    private final int localNum;
    
    public RecyclingBag(final int localNum, final long publicNum, final Supplier<? extends T> supplier) {
    	this.supplier = supplier;
        this.localNum = localNum;
        this.localVar = ThreadLocal.withInitial(() -> {
            final ArrayQueue<T> queue = new ArrayQueue<T>(localNum);
            
            for(int i = 0;i<localNum;i++) {
                T x = supplier.get();
                queue.add(x);
            }
            
            return queue;
        });
        
        for(int i = 0; i < publicNum; i++) {
            T x = supplier.get();
            this.publicPool.add(x);
        }
    }
    
    public T get() {
    	ArrayQueue<T> queue = this.localVar.get();
        T x = queue.take();
        if(x != null) {
            return x;
        }
        
        x = this.publicPool.poll();
        if(x != null) {
            return x;
        }
        
        return supplier.get();
    }
    
    public void release(T obj) {
        ArrayQueue<T> queue = this.localVar.get();
        if(queue.size() >= this.localNum) {
            this.publicPool.add(obj);
            return ;
        }
        queue.release(obj);
    }
}
