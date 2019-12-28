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

    public String serialize(Object object) throws Exception.MissingPHPClassAnnotation {
         this.objectsList = new ArrayList<>();
         return this.serializeObject(object);
    }

    private String serializeObject(Object object) throws Exception.MissingPHPClassAnnotation {
        if (object == null) {
            return NULL;
        }
        Class<?> oClass = object.getClass();
        String className = oClass.getName();
        switch(className) {
            case "java.lang.Integer":
                return serialize((Integer)object);

            case "java.lang.String":
                return serialize((String)object);
        }

        if (!oClass.isAnnotationPresent(PHPClass.class)) {
            throw new Exception.MissingPHPClassAnnotation(oClass.getName());
        }

        StringBuilder objectString = new StringBuilder();

        String phpClass = null;

        Annotation[] declaredAnnotations = oClass.getDeclaredAnnotations();

        for(Annotation a : declaredAnnotations) {
            Class<? extends Annotation> annotationType = a.annotationType();
            if(annotationType.getName().equals(PHPClass.class.getName())) {
                PHPClass p = oClass.getAnnotation(PHPClass.class);
                phpClass = p.value();
                break;
            }
        }

        if (phpClass == null) {
            throw new Exception.MissingPHPClassAnnotation(className);
        }

        objectString.append("O:")
                .append(phpClass.length())
                .append(":\"")
                .append(phpClass)
                .append("\":");

        Field[] allFields = object.getClass().getDeclaredFields();

        ArrayList<PropertiesReference> rerferenceList = new ArrayList<>();

        for(Field field : allFields) {
            int modifiers = field.getModifiers();
            String fieldName = field.getName();
            if (Modifier.isProtected(modifiers)) {
                field.setAccessible(true);
                fieldName = "s:"
                        + (fieldName.length() + 3)
                        + ":\"\0*\0"
                        + fieldName
                        + "\";";
            } else if (Modifier.isPrivate(modifiers)) {
                field.setAccessible(true);
                String privateClassString = "\0" + phpClass + "\0" + fieldName;
                fieldName = "s:"
                        + privateClassString.length()
                        + ":\""
                        + privateClassString
                        + "\";";
            } else {
                fieldName = "s:"
                        + fieldName.length()
                        + ":\""
                        + fieldName
                        + "\";";
            }
            try {
                Object fieldData = field.get(object);
                int j = 0;
                boolean addedRef = false;
                for(Object pValue : objectsList) {
                    if (fieldData == pValue) {
                        rerferenceList.add(new PropertiesReference(j + 2, field.getName()));
                        addedRef = true;
                        break;
                    }
                    j++;
                }
                if (!addedRef) {
                    objectsList.add(fieldData);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        objectString.append(allFields.length).append(":{");

        for (Field field : allFields) {
            int modifiers = field.getModifiers();
            String fieldName = field.getName();
            if (Modifier.isProtected(modifiers)) {
                field.setAccessible(true);
                objectString.append("s:")
                        .append(fieldName.length() + 3)
                        .append(":\"\0*\0")
                        .append(fieldName)
                        .append("\";");
            } else if (Modifier.isPrivate(modifiers)) {
                field.setAccessible(true);
                String privateClassString = "\0" + phpClass + "\0" + fieldName;
                objectString.append("s:")
                        .append(privateClassString.length())
                        .append(":\"")
                        .append(privateClassString)
                        .append("\";");
            } else {
                objectString.append("s:")
                        .append(fieldName.length())
                        .append(":\"")
                        .append(fieldName)
                        .append("\";");
            }

            try {
                objectString.append(serialize(field.get(object)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        objectString.append("}");
        return objectString.toString();
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
}
