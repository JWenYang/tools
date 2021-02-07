package com.xuelei.tools.designpattern;

public class Singleton {

    //私有构造方法，防止被实例化
    private Singleton(){}

    //使用内部类维护单例
    private static class SingletonFactory{
        private static Singleton singleton = new Singleton();
    }

    //获取实例
    public static Singleton getInstance(){
        return SingletonFactory.singleton;
    }

    //序列化一直
    public Object readResolve(){
        return getInstance();
    }
}
