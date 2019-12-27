package com.wandell.PHP;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class Serializer {

    private static final String NULL = "N;";

    public static String serialize(byte[] data) {
        String b = Arrays.toString(data);
        return "s:" + b.length() + ";";
    }

    public static String serialize(byte data) {
        String b = Byte.toString(data);
        return "s:" + b.length() + ";";
    }

    public static String serialize(String data) {
        return "s:" + data.length() + ":\"" + data + "\";";
    }

    public static String serialize(int data) {
        return "i:" + data + ";";
    }

    public static String serialize(Integer data) {
        return "i:" + data + ";";
    }

    public static String serialize(boolean data) {
        return "b:" + (data ? "1" : "0") + ";";
    }

    public static String serialize(Object data) throws Exception.MissingPHPClassAnnotation {
        if (data == null) {
            return NULL;
        }
        Class<?> aClass = data.getClass();
        String className = aClass.getName();
        switch(className) {
            case "java.lang.Integer":
                return serialize((Integer)data);

            case "java.lang.String":
                return serialize((String)data);
        }

        if (aClass.isAnnotationPresent(PHPClass.class)) {
            StringBuilder objectString = new StringBuilder();

            String phpClass = null;

            Annotation[] declaredAnnotations = aClass.getDeclaredAnnotations();

            for(Annotation a : declaredAnnotations) {
                Class<? extends Annotation> annotationType = a.annotationType();
                if(annotationType.getName().equals(PHPClass.class.getName())) {
                    PHPClass p = aClass.getAnnotation(PHPClass.class);
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

            Field[] allFields = data.getClass().getDeclaredFields();
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
                    objectString.append(Serializer.serialize(field.get(data)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            objectString.append("}");
            return objectString.toString();
        } else {
            throw new Exception.MissingPHPClassAnnotation(aClass.getName());
        }
    }

    public static <T>String serialize(T[] data) throws Exception.MissingPHPClassAnnotation {
        StringBuilder string = new StringBuilder();
        string.append("a:").append(data.length).append(":{");

        for(Object e : data) {
            string.append(Serializer.serialize(e));
        }
        string.append("}");
        return string.toString();
    }
}
