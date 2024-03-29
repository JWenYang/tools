package com.xuelei.tools;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

public class Test_ {

    @Test
    public void test0(){

        double[] tw = {34.5,37.5,35.6,38.1,34.5,34.6,39.8};

        Stack<Integer> l = new Stack<>();

        for(int i = 0 ; i < tw.length ; i++){
            if(tw[i]>36 && !l.empty()){
                System.out.println(i-l.pop());
            }else{
                System.out.println(0);
            }

            if(tw[i]>36){
                l.push(i);
            }
        }

    }


    @Test
    public void test1(){
        String numRegex = "[0-9|\\s]12313";
        String sql = " 12313";
        boolean matches = Pattern.matches(numRegex, sql);
        System.out.println(matches);


        Pattern compile = Pattern.compile(numRegex);
        System.out.println(compile.matcher(sql).find());
//        if(ReUtil.contains(numRegex,sql)){
//            System.out.println(0);
//        }
    }

    @Test
    public void test2(){
        Node node0 = new Node();
        node0.setV(2);
        Node node1 = new Node();
        node1.setV(2);
        Node node2 = new Node();
        node2.setV(4);
        node0.setR(node1);
        node0.setL(node2);
        int sum = sum(node0);
        System.out.println(sum);
    }

    @Test
    public void test3(){
        Node node0 = new Node();
        node0.setV(2);
        Node node1 = new Node();
        node1.setV(2);
        Node node2 = new Node();
        node2.setV(4);
        node0.setR(node1);
        node0.setL(node2);
        List<Integer> list = new ArrayList<>();
        sum0(node0,list);
        int sum = 0;
        for(Integer key:list)
            sum+=key;
        System.out.println(sum);
    }

    public void sum0(Node treeNode, List<Integer> list) {
        Stack<Node> stack = new Stack<>();
        while (treeNode != null || !stack.isEmpty()) {
            while (treeNode != null) {
                list.add(treeNode.v);
                stack.push(treeNode);
                treeNode = treeNode.l;
            }
            if(!stack.isEmpty()){
                treeNode = stack.pop();
                treeNode = treeNode.r;
            }
        }
    }

    public int sum(Node root) {
        List<Integer> list = new ArrayList<>();
        sumList(root,list);
        int sum = 0;
        for(Integer key:list)
            sum+=key;
        return sum;
    }

    public static void sumList(Node root, List<Integer> list){
        if(root==null)
            return ;
        else{
            list.add(root.v);
            sumList(root.l,list);
            sumList(root.r,list);
        }
    }

}
