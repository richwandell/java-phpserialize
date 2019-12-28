package com.wandell.PHP;

public class PropertyValue {


    private final String name;
    private final Object value;

    public PropertyValue(String name, Object value) {

        this.name = name;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
