package com.zanebono.chart.Utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext Context=null;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.Context=applicationContext;
    }
    public static <T> T getBean(String beanName){
        System.out.println(beanName);
        return (T)Context.getBean(beanName);
    }
}
