package com.example.RPC;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultFuture {
    public static ConcurrentHashMap<Long,DefaultFuture>map=new ConcurrentHashMap<>();
    final Lock lock=new ReentrantLock();
    public Condition condition = lock.newCondition();
    private Response response;
    public DefaultFuture(ClientRequest request){
        map.put(request.getId(),this);
    }

    public static void receive(Response response){
        DefaultFuture df = map.get(response.getId());
        System.out.println(response.getId());
        if (df!=null){
            df.lock.lock();
            try {
                df.setResponse(response);
                df.condition.signal();
                map.remove(response.getId());
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                df.lock.unlock();
            }
        }
    }
    public Response get(){
        lock.lock();
        try {
            while (response==null){
                condition.await();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
