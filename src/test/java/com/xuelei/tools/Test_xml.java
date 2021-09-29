package com.xuelei.tools;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.StringJoiner;

public class Test_xml {

    @Test
    public void xmlToObj(){
        String xml = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<USERTASKSBATCHQUERY_REQ>\n" +
                "<EMPIDSORUSERIDS>12584,12569,18945</EMPIDSORUSERIDS>\n" +
                "<SYSCODE>CHNTCMS</SYSCODE>\n" +
                "<PAGESIZE>10</PAGESIZE>\n" +
                "<CURPAGE>1</CURPAGE>\n" +
                "<CONTRACTID>771234e9-3c3d-4e2b-933b-fd03708fb546</CONTRACTID>\n" +
                "<TASKTITLE>禾湟记（一品鲜）基站租赁合同</TASKTITLE>\n" +
                "<PARAM1>1</PARAM1>\n" +
                "<PARAM1>2</PARAM1>\n" +
                "<PARAM1>3</PARAM1>\n" +
                "</USERTASKSBATCHQUERY_REQ>\n";
        Object xmlObject = null;
        try {
            JAXBContext context = JAXBContext.newInstance(TestObj.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader fr = new StringReader(xml);
            xmlObject = unmarshaller.unmarshal(fr);
            System.out.println(JSONUtil.toJsonPrettyStr(xmlObject));
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void strTest(){
        StringJoiner stringJoiner = new StringJoiner(",");
        String[] split = "123,123,123".split(",");
        for (String param : split) {
            stringJoiner.add(StrUtil.format("'{}'",param));
        }
        System.out.println(stringJoiner.toString());

        String a = ",123,123,123,";
        System.out.println(a.substring(0,a.length()-1));

        StringBuilder stringBuilder = new StringBuilder();
        String[] split1 = "123,123,123".split(",");
        for (int i = 0; i < split1.length; i++) {
            stringBuilder.append("'");
            stringBuilder.append(split1[i]);
            stringBuilder.append("'");
            if(i == split1.length-1){
                break;
            }
            stringBuilder.append(",");
        }
        System.out.println(stringJoiner.toString());
    }

    @Test
    public void BeanGenTest(){
        BeanGenerator beanGenerator = new BeanGenerator();
        beanGenerator.setSuperclass(TestObj.class);
        beanGenerator.addProperty("status", String.class);
        Object o = beanGenerator.create();
        BeanMap beanMap = BeanMap.create(o);
        beanMap.put("status", "01");
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            System.out.println(objectMapper.writeValueAsString(o));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
