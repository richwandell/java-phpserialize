package com.wandell.PHP;

class PropertiesReference {
    private int intKey;
    private int refNum;
    private String key;

    public PropertiesReference(int refNum, String key) {
        this.refNum = refNum;
        this.key = key;
    }

    public PropertiesReference(int refNum) {
        this.refNum = refNum;
    }

    public int getRefNum() {
        return refNum;
    }

    public String getKey() {
        return key;
    }

    public int getIntKey() {
        return intKey;
    }
}
