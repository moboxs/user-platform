package com.github.moboxs.httpclient;

public class Client {
    public static void main(String[] args) {
        Integer aa = 127;
        Integer bb = 127;
        System.out.println(aa == bb);
        aa = 129;
        bb = 129;

        System.out.println(aa > bb);

    }

    public <T> T com() {

        T t = (T) Integer.valueOf(1);

        return t;
    }
}
