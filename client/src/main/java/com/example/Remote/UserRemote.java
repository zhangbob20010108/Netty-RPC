package com.example.Remote;


import com.example.client.Response;
import com.example.client.User.User;

public interface UserRemote {
    public Response save(User user);
    public void saveTest(User user);
}
