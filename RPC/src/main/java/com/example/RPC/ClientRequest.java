package com.example.RPC;

import java.util.concurrent.atomic.AtomicInteger;

public class ClientRequest {
    private long id;
    private Object content;
    private static AtomicInteger aid=new AtomicInteger(1);

    private String command;

    public ClientRequest(){
        id=aid.getAndIncrement();
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
    public long getId() {
        return id;
    }

    public String getCommand() {
        return command;
    }
    public void setCommand(String command) {
        this.command = command;
    }
}
