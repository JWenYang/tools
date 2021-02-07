package com.xuelei.tools.redis.enums;

public enum Enums {

    EXPIRED0("order_expire");

    private String prefix;

    Enums(String prefix) {
       this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
