package com.example.RPC.User;

import com.example.RPC.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
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
