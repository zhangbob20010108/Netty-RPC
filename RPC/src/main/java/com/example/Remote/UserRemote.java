package com.example.Remote;

import com.example.RPC.Response;
import com.example.RPC.User.User;

public interface UserRemote {
    public Response save(User user);
    public void saveTest(User user);
}
