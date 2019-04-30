package io.github.laplacedemon.recyclingbag;

import java.util.ArrayList;
import java.util.List;

public class ArrayQueue<T> {
    private List<T> list;
    private int putIndex;
    private int takeIndex;
    private int size;
    private int cap;
    
    public ArrayQueue(int cap) {
        this.list = new ArrayList<>(cap);
        this.cap = cap;
    }
    
    public void add(T obj) {
        this.list.add(obj);
        ++this.size;
    }
    
    public void release(T obj) {
        if(this.size < this.cap) {
            this.list.set(this.putIndex, obj);
            ++this.size;
            ++this.putIndex;
            if(this.putIndex == this.cap) {
                this.putIndex = 0;
            }
        } else {
            throw new RuntimeException();
        }
    }
    
    public T take() {
        if(this.size > 0) {
            T obj = this.list.get(this.takeIndex);
            ++this.takeIndex;
            --this.size;
            if(this.takeIndex == this.cap) {
                this.takeIndex = 0;
            }
            return obj;
        }
        
        return null;
    }

    public int size() {
        return size;
    }

    public int getCap() {
        return cap;
    }
    
}
