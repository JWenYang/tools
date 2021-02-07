package com.xuelei.tools.designpattern;

import java.util.Vector;

public class Singleton0 {

    private static Singleton0 singleton0 = null;
    private Vector v = null;

    private Singleton0(){}

    private static void syncInit(){
        if(null == singleton0){
            singleton0 = new Singleton0();
        }
    }

    private static Singleton0 getInstance(){
        if(null == singleton0){
            syncInit();
        }
        return singleton0;
    }
}
