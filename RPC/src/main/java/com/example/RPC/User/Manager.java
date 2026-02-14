package com.example.RPC.User;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.RPC.ClientRequest;
import com.example.RPC.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Manager {
    public static Map<String,BeanMethod>beanMap;
    static {
        beanMap=new HashMap<>();
    }

    public static Response process(ClientRequest request){
        Response response=null;
        try {
            String command = request.getCommand();
            BeanMethod beanMethod = beanMap.get(command);
            System.out.println(command);
            if (beanMethod == null) {
                System.out.println("is null");
                return null;
            }
            Object bean = beanMethod.getBean();
            Method method = beanMethod.getM();
            Class parameterType = method.getParameterTypes()[0];
            Object content=request.getContent();
            Object params = JSONObject.parseObject(JSONObject.toJSONString(content), parameterType);
            response =(Response) method.invoke(bean, params);
            response.setId(request.getId());
            System.out.println(response.getId());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return response;
    }
}
