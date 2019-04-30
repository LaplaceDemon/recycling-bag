package io.github.laplacedemon.recyclingbag;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import stormpot.Allocator;
import stormpot.BlazePool;
import stormpot.Config;
import stormpot.Pool;
import stormpot.PoolException;
import stormpot.Poolable;
import stormpot.Slot;
import stormpot.Timeout;

public class TestStormpot {
    
    @Test
    public void testNew() throws PoolException, InterruptedException {
        MyObjectFactory allocator = new MyObjectFactory();
        Config<MyObject> config = new Config<MyObject>().setAllocator(allocator);
        Pool<MyObject> pool = new BlazePool<MyObject>(config);
        
        Timeout timeout = new Timeout(1, TimeUnit.DAYS);

        MyObject object = pool.claim(timeout);
        try {
            System.out.println("use:" + object.id);
        } finally {
          if (object != null) {
            object.release();
          }
        }
    }
}

class MyObject implements Poolable {
    
    public long id;

    @Override
    public void release() {
        System.out.println(this.id + " been deleted!");
    }

}

class MyObjectFactory implements Allocator<MyObject> {

    @Override
    public MyObject allocate(Slot slot) throws Exception {
        MyObject myObject = new MyObject();
        myObject.id = (long)(Math.random()*100000);
        return myObject;
    }

    @Override
    public void deallocate(MyObject poolable) throws Exception {
        System.out.println("deallocate " + poolable.id);
    }
    
}
