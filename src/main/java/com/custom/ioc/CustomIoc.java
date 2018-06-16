package com.custom.ioc;

import com.custom.ioc.ext.ExtClassPathXmlApplicationContext;
import com.custom.ioc.service.CustomService;
import org.dom4j.DocumentException;

public class CustomIoc {

    public static void main(String[] args) throws Exception {
        //创建ExtClassPathXmlApplicationContext对象
        ExtClassPathXmlApplicationContext classPath = new ExtClassPathXmlApplicationContext("spring.xml");
        CustomService customService = (CustomService)classPath.getBean("myCustomBean");
        customService.CustomMethod();
    }

}
