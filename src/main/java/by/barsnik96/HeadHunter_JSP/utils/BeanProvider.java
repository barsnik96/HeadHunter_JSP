package by.barsnik96.HeadHunter_JSP.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanProvider {
    public static ApplicationContext applicationContext;

    // Autowires the specified object in the spring context
    public static void autowire(Object object) {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(object);
    }

    @Autowired
    private void setApplicationContext(ApplicationContext applicationContext) {
        BeanProvider.applicationContext = applicationContext;
    }
}