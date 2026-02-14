package com.example.Remote;

import com.example.RPC.User.BeanMethod;
import com.example.RPC.User.Manager;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class RemoteMedium implements BeanPostProcessor {
    @Override
    public @Nullable Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public @Nullable Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Remote.class)){
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method:methods){
                String command =bean.getClass().getInterfaces()[0].getName()+"."+method.getName();
                System.out.println(command);
                BeanMethod beanMethod = new BeanMethod();
                beanMethod.setBean(bean);
                beanMethod.setM(method);
                Manager.beanMap.put(command,beanMethod);
            }
        }
        return bean;
    }
}
