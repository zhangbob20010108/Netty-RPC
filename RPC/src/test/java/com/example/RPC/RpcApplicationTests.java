package com.example.RPC;

import com.example.Remote.RemoteInvoke;
import com.example.Remote.UserRemote;
import com.example.RPC.User.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RpcApplicationTests.class)
@ComponentScan("com.example.RPC")
public class RpcApplicationTests {
	@RemoteInvoke
	private UserRemote remote;
	@Test
	public void contextLoads() {
	}
	@Test
	public void testSaveUser(){
		User user=new User();
		user.setId(1);
		user.setName("BOB");
		ClientRequest request=new ClientRequest();
		request.setCommand("com.example.RPC.User.UserController.save");
		request.setContent(user);
		Response response = TCPClient.send(request);
		System.out.println(response.getResult());
		System.out.println(response.getStatus());
	}


}
