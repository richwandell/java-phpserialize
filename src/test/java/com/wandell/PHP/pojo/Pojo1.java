package com.wandell.PHP.pojo;

import com.wandell.PHP.PHPClass;

@PHPClass("wandell\\Example\\Popo\\Popo1")
public class Pojo1 {

    public Pojo1 a;

    public int b;

    private String c;

    protected int d;

    public Integer e;

    private Double f;

    protected boolean g;

    public Pojo1() {
        super();
    }

    public Pojo1(String c, int d) {
        this.c = c;
        this.d = d;
    }

    public String getC() {
        return c;
    }

    public int getD() {
        return d;
    }

    public Double getF() {
        return f;
    }

    public boolean getG() {
        return g;
    }
}
