package com.example.Remote;

import com.example.RPC.Response;
import com.example.RPC.User.User;
import com.example.RPC.User.Userservice;
import org.springframework.beans.factory.annotation.Autowired;

@Remote
public class UserRemoteImpl implements UserRemote{

    @Autowired
    private Userservice service;
    public Response save(User user){
        service.saveUser(user);
        Response response= new Response();
        response.setResult(user);
        response.setStatus("Sucess");
        return response;
    }
    public void saveTest(User user){
        service.saveUser(user);
    }
}
