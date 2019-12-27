package com.wandell.PHP.pojo;

import com.wandell.PHP.PHPClass;

@PHPClass("wandell\\Example\\Popo\\Popo2")
public class Pojo2 {

    private Pojo1 object1;

    private Pojo1 object2;

    public Pojo2() {
        super();
    }

    public Pojo2(Pojo1 object1, Pojo1 object2) {
        this.object1 = object1;
        this.object2 = object2;
    }

    public Pojo1 getObject1() {
        return object1;
    }

    public Pojo1 getObject2() {
        return object2;
    }
}

