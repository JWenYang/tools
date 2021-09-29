package com.xuelei.tools;


import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
// XML文件中的根标识
@XmlRootElement(name = "USERTASKSBATCHQUERY_REQ")
public class TestObj {

    @XmlElement(name = "EMPIDSORUSERIDS")
    private String s01;

    @XmlElement(name = "SYSCODE")
    private String s02;

    public String getS01() {
        return s01;
    }

    public void setS01(String s01) {
        this.s01 = s01;
    }

    public String getS02() {
        return s02;
    }

    public void setS02(String s02) {
        this.s02 = s02;
    }
}
