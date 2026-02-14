package com.example.Remote;

import com.example.client.ClientRequest;
import com.example.client.Response;
import com.example.client.TCPClient;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Component
public class InvokeProxy implements BeanPostProcessor {
    @Override
    public @Nullable Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field:fields){
            if (field.isAnnotationPresent(RemoteInvoke.class)){
                field.setAccessible(true);

                Object proxyInstance =  Proxy.newProxyInstance(
                        field.getType().getClassLoader(),new Class[]{
                        field.getType()
                },(proxy, method, args)->{
                    ClientRequest request=new ClientRequest();
                    request.setCommand(field.getType().getName()+"."+method.getName());
                    System.out.println(field.getType().getName()+"."+method.getName());
                    request.setContent(args[0]);
                    System.out.println(args[0]);
                    Response response = TCPClient.send(request);
                    return response;
                });
                try {
                    field.set(bean, proxyInstance);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bean;
    }

    @Override
    public @Nullable Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
