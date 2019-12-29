package com.wandell.PHP;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

public class Serializer {

    private static final String NULL = "N;";

    private ArrayList<Object> objectsList = new ArrayList<>();

    public Serializer() {
        super();
    }

    public String serialize(byte[] data) {
        String b = Arrays.toString(data);
        return "s:" + b.length() + ";";
    }

    public String serialize(byte data) {
        String b = Byte.toString(data);
        return "s:" + b.length() + ";";
    }

    public String serialize(String data) {
        return "s:" + data.length() + ":\"" + data + "\";";
    }

    public String serialize(int data) {
        return "i:" + data + ";";
    }

    public String serialize(Integer data) {
        return "i:" + data + ";";
    }

    public String serialize(boolean data) {
        return "b:" + (data ? "1" : "0") + ";";
    }

    public String serialize(Boolean data) {
        return "b:" + (data ? "1" : "0") + ";";
    }

    public String serialize(Object object) throws Exception.MissingPHPClassAnnotation {
         this.objectsList = new ArrayList<>();
         return this.iSerialize(object);
    }

    public <T>String serialize(T[] data) throws Exception.MissingPHPClassAnnotation {
        StringBuilder string = new StringBuilder();
        string.append("a:").append(data.length).append(":{");

        for(Object e : data) {
            string.append(serialize(e));
        }
        string.append("}");
        return string.toString();
    }

    private String iSerialize(Object object) throws Exception.MissingPHPClassAnnotation {
        if (object == null) {
            return NULL;
        }
        if (objectsList.contains(object)) {
            return "r:" + (objectsList.indexOf(object) + 1) + ";";
        }

        Class<?> oClass = object.getClass();
        String className = oClass.getName();
        switch(className) {
            case "java.lang.Integer":
                return iSerialize((Integer)object);

            case "java.lang.String":
                return iSerialize((String)object);

            case "java.lang.Boolean":
                return iSerialize((Boolean)object);
        }

        objectsList.add(object);

        if (!oClass.isAnnotationPresent(PHPClass.class)) {
            throw new Exception.MissingPHPClassAnnotation(oClass.getName());
        }

        String phpClass = null;

        Annotation[] declaredAnnotations = oClass.getDeclaredAnnotations();

        for(Annotation a : declaredAnnotations) {
            Class<? extends Annotation> annotationType = a.annotationType();
            if(annotationType == PHPClass.class) {
                PHPClass p = oClass.getAnnotation(PHPClass.class);
                phpClass = p.value();
                break;
            }
        }

        if (phpClass == null) {
            throw new Exception.MissingPHPClassAnnotation(className);
        }

        String objectString = "O:" + phpClass.length() + ":\"" + phpClass + "\":";
        Field[] allFields = object.getClass().getDeclaredFields();
        objectString += allFields.length + ":{";
        ArrayList<PropertiesReference> references = new ArrayList<>();

        for(Field field : allFields) {
            int modifiers = field.getModifiers();
            String fieldName = field.getName();
            String fieldValue = "";
            if (Modifier.isProtected(modifiers)) {
                field.setAccessible(true);
                fieldName = "s:" + (fieldName.length() + 3) + ":\"\0*\0" + fieldName + "\";";
            } else if (Modifier.isPrivate(modifiers)) {
                field.setAccessible(true);
                String privateClassString = "\0" + phpClass + "\0" + fieldName;
                fieldName = "s:" + privateClassString.length() + ":\"" + privateClassString + "\";";
            } else {
                fieldName = "s:" + fieldName.length() + ":\"" + fieldName + "\";";
            }
            try {
                Object fieldData = field.get(object);
                if (objectsList.contains(fieldData)) {
                    fieldValue = "r:" + (objectsList.indexOf(fieldData) + 1) + ";";
                    references.add(new PropertiesReference(fieldName, fieldValue));
                } else {
                    fieldValue = iSerialize(fieldData);
                    if (
                            fieldData != null
                            && !(fieldData instanceof Integer)
                            && !objectsList.contains(fieldData)
                    ) {
                        objectsList.add(fieldData);
                    }
                    objectString += fieldName + fieldValue;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        for(PropertiesReference ref : references) {
            objectString += ref.getFieldName() + ref.getFieldValue();
        }
        objectString += "}";
        return objectString.toString();
    }

    private String iSerialize(byte[] data) {
        return serialize(data);
    }

    private String iSerialize(int data) {
        return serialize(data);
    }

    private String iSerialize(byte data) {
        return serialize(data);
    }

    private String iSerialize(Integer data) {
        return serialize(data);
    }

    private String iSerialize(String data) {
        return serialize(data);
    }

    private String iSerialize(boolean data) {
        return serialize(data);
    }

    private String iSerialize(Boolean data) {
        return serialize(data);
    }
}
