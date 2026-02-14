package com.example.client;


import java.util.Enumeration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultFuture {
    public static ConcurrentHashMap<Long,DefaultFuture>map=new ConcurrentHashMap<>();
    final Lock lock=new ReentrantLock();
    public Condition condition = lock.newCondition();
    private Response response;

    private long timeOut=2*60*1000;
    private long startTime=System.currentTimeMillis();

    public DefaultFuture(ClientRequest request){
        map.put(request.getId(),this);
    }
    static class FutureThread extends Thread{
        @Override
        public void run() {
            Set<Long> keys = map.keySet();
            for (Long id:keys){
                DefaultFuture df = map.get(id);
                if (System.currentTimeMillis()- df.startTime>df.timeOut){
                    Response response=new Response();
                    response.setId(id);
                    response.setResult("Timeout");
                    receive(response);
                }
            }
        }
    }
    static {
        FutureThread thread=new FutureThread();
        thread.setDaemon(true);
        thread.start();
    }

    public static void receive(Response response){
        DefaultFuture df = map.get(response.getId());
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
    public Response get(long time_wait){
        lock.lock();
        try {
            while (response==null){
                if (System.currentTimeMillis()-startTime>time_wait){
                    System.out.println("TIMEOUT!");
                    break;
                }
                condition.await(time_wait, TimeUnit.SECONDS);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return response;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
