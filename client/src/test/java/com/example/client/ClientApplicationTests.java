package com.example.client;

import com.example.Remote.RemoteInvoke;
import com.example.Remote.UserRemote;
import com.example.client.User.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ClientApplicationTests.class)
@ComponentScan("com.example")
public class ClientApplicationTests {

	@RemoteInvoke
	private UserRemote remote;

	@Test
	public void testRemote(){
		User user=new User();
		user.setId(1);
		user.setName("BOB");
		Response response = remote.save(user);
		System.out.println("ok");
	}

}
