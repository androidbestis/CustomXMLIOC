package com.custom.ioc.ext;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 * 自定义ClassPathXMLApplicationContext
 */
public class ExtClassPathXmlApplicationContext {

    private String xmlpath = null;

    //初始化
    public ExtClassPathXmlApplicationContext(String xmlpath) throws DocumentException {
      this.xmlpath = xmlpath;
    }

    //获取当前上下文路径
   public InputStream getResourceAsStream(String xmlpath){
       InputStream inpustream = this.getClass().getClassLoader().getResourceAsStream(xmlpath);
       return inpustream;
    }

   //解析XML
    public List<Element>  readerXml() throws DocumentException {
        //1.解析xml文件信息
        SAXReader reader = new SAXReader();
        Document document = reader.read(getResourceAsStream(xmlpath));
        //2.获取根节点
        Element rootElement = document.getRootElement();
        //获取根节点下所有子节点
        List<Element> elements = rootElement.elements();
        return elements;
    }

    //初始化对象
    public Object newInstance(String classname) throws Exception {
        Class<?> classInfo = Class.forName(classname);
        return classInfo.newInstance();
    }

    public String findByElementClass(List<Element> elements,String beanId){
        for (Element  element: elements) {
            //获取属性信息
            String xmlBeanId = element.attributeValue("id");
            if(StringUtils.isEmpty(xmlBeanId)){
                continue;
            }
            if(xmlBeanId.equals(beanId)){
                String xmlClass = element.attributeValue("class");
                return xmlClass;
            }
        }
      return null;
    }


    public Object getBean(String beanId) throws Exception {
        if(StringUtils.isEmpty(beanId)){
            throw new Exception("beanId 不能为空!");
        }
        //1.解析bean文件,获取所有bean的节点信息
        List<Element> elements = readerXml();
        if(elements == null || elements.isEmpty()){
            throw new Exception("配置文件中,没有配置Bean信息");
        }
        //2.方法参数的beanId和配置文件中子节点beanId是否一致
        String classInfo = findByElementClass(elements, beanId);
        if(StringUtils.isEmpty(classInfo)){
            throw new Exception("class属性不存在");
        }
        //3.通过class包地址信息,使用反射机制初始化实例
        Object beaninstace = newInstance(classInfo);
        return beaninstace;
    }

}
