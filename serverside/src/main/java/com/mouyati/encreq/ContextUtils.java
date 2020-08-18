package com.mouyati.encreq;


import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


public class ContextUtils implements ApplicationListener<ContextRefreshedEvent> {
    private static ApplicationContext context;

    public static ApplicationContext getContext() {
        return context;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.context = event.getApplicationContext();
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static Object getBean(String beanName){
        return context.getBean(beanName);
    }

}
