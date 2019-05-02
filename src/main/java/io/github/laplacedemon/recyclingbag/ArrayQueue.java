package io.github.laplacedemon.recyclingbag;

public class ArrayQueue<T> {
    private Object[] array;
    private int arrayIndex;
    private int putIndex;
    private int takeIndex;
    private int size;
    private int cap;
    
    public ArrayQueue(int cap) {
        this.array = new Object[cap];
        this.cap = cap;
    }
    
    public void add(T obj) {
        this.array[this.arrayIndex] = obj;
        ++this.arrayIndex;
        ++this.size;
    }
    
    public void release(T obj) {
        if(this.size < this.cap) {
            this.array[this.putIndex] = obj;
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
            @SuppressWarnings("unchecked")
			T obj = (T) this.array[this.takeIndex];
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
