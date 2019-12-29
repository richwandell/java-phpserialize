package com.wandell.PHP;

class PropertiesReference {
    private String fieldName;
    private String fieldValue;
    private int refNum;
    private String key;

    public PropertiesReference(int refNum, String key) {
        this.refNum = refNum;
        this.key = key;
    }

    public PropertiesReference(String fieldName, String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public int getRefNum() {
        return refNum;
    }

    public String getKey() {
        return key;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }
}
