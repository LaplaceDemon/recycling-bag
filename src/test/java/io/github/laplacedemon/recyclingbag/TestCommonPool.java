package io.github.laplacedemon.recyclingbag;

import java.util.NoSuchElementException;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.junit.Test;


public class TestCommonPool {
    
    @Test
    public void testNew() throws NoSuchElementException, IllegalStateException, Exception {
        ObjectPool<Object> pool = new GenericObjectPool<Object>(new ObjectFactory());
        
        long size = 10000*10000L;
        
        long t0 = System.currentTimeMillis();
        for(long i = 0;i<size;i++) {
            Object obj = pool.borrowObject();
            pool.returnObject(obj);
        }
        long t1 = System.currentTimeMillis();
        double dt = t1-t0;
        System.out.println(size*1000/dt);
        
        pool.close();
    }
}

class ObjectFactory extends BasePooledObjectFactory<Object> {

    @Override
    public Object create() throws Exception {
        return new Object();
    }

    @Override
    public PooledObject<Object> wrap(Object obj) {
        return new DefaultPooledObject<Object>(obj);
    }
    
}