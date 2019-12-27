package com.wandell.PHP;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

@SuppressWarnings("Duplicates")
public class Unserializer {

    private Class<?>[] classes;

    private int index = 0;
    private boolean hasReferences = false;

    public Unserializer() {
        super();
    }

    public Unserializer(Class<?>[] classes) throws Exception.MissingPHPClassAnnotation {
        for(Class<?> c : classes) {
            if (!c.isAnnotationPresent(PHPClass.class)) {
                throw new Exception.MissingPHPClassAnnotation(c.getName());
            }
        }
        this.classes = classes;
    }

    public Object unserialize(String data) throws Exception.POJORequiresEmptyConstructorException {
        if (data.charAt(0) == 'O') {
            Object val = unserializeObject(data);
            return val;
        } else if (data.charAt(0) == 'a') {
            Object val = unserializeArray(data);
            return val;
        }
        return null;
    }

    private Float getFloatVal(String data) {
        boolean inNumber = false;
        Float val = null;
        int intStart = 0;
        for(int i = index; i < data.length(); i++) {
            char ch = data.charAt(i);
            if (!inNumber && ch == ':') {
                inNumber = true;
                intStart = i + 1;
            } else if (inNumber && ch == ';') {
                val = Float.parseFloat(data.substring(intStart, i));
                index = i;
                break;
            }
        }
        return val;
    }

    private Integer getIntegerVal(String data) {
        boolean inNumber = false;
        Integer in = null;
        int intStart = 0;
        for(int i = index; i < data.length(); i++) {
            char ch = data.charAt(i);
            if (!inNumber && ch == ':') {
                inNumber = true;
                intStart = i + 1;
            } else if (inNumber && ch == ';') {
                in = Integer.parseInt(data.substring(intStart, i));
                index = i;
                break;
            }
        }
        return in;
    }

    private String getIntegerString(String data) {
        boolean inNumber = false;
        String in = "";
        int intStart = 0;
        for(int i = index; i < data.length(); i++) {
            char ch = data.charAt(i);
            if (!inNumber && ch == ':') {
                inNumber = true;
                intStart = i + 1;
            } else if (inNumber && ch == ';') {
                in = data.substring(intStart, i);
                index = i;
                break;
            }
        }
        return in;
    }

    private String getString(String data) {
        boolean inString = false;
        String s = "";
        int stringStart = 0;
        for(int i = index; i < data.length(); i++) {
            char ch = data.charAt(i);
            if (!inString && ch == '"') {
                inString = true;
                stringStart = i + 1;
            } else if (inString && ch == '"') {
                s = data.substring(stringStart, i);
                index = i + 1;
                break;
            }
        }
        return s;
    }

    private String getObjectType(String data) {
        return getString(data);
    }

    private String getKeyName(String data) {
        if (data.charAt(index) == 'i') {
            return getIntegerString(data);
        }
        String stringData = getString(data);
        return stringData;
    }

    private Object getStringVal(String data) {
        return getString(data);
    }

    private String getRealKey(String data) {
        if (data.length() == 1) {
            return data;
        }
        String realKey = "";
        char ch;
        for(int i = data.length() - 1; i > -1; i--) {
            ch = data.charAt(i);
            if (ch == '\0') {
                realKey = data.substring(i + 1, data.length());
                break;
            } else if (i == 0) {
                realKey = data.substring(i, data.length());
                break;
            }
        }
        return realKey;
    }

    private static class PropertiesReference {
        private int refNum;
        private String key;

        public PropertiesReference(int refNum, String key) {
            this.refNum = refNum;
            this.key = key;
        }

        public int getRefNum() {
            return refNum;
        }

        public String getKey() {
            return key;
        }
    }

    public LinkedHashMap<String, Object> getObjectProperties(String data) throws Exception.POJORequiresEmptyConstructorException {
        LinkedHashMap<String, Object> objectProperties = new LinkedHashMap<>();
        char ch;
        boolean inObject = false;
        boolean readingKey = false;
        boolean readingVal = false;
        ArrayList<PropertiesReference> referencesList = new ArrayList<>();
        ArrayList<String> keysList = new ArrayList<>();
        String key = "";
        while(true) {
            ch = data.charAt(index);
            if (inObject) {
                if (readingKey) {
                    if (ch == '}') {
                        break;
                    }
                    key = getRealKey(getKeyName(data));
                    keysList.add((String)key);
                    readingKey = false;
                    readingVal = true;
                } else if (readingVal) {
                    Object val = null;
                    if (ch == 'O') {
                        val = unserializeObject(data);
                    } else if (ch == 'a') {
                        val = unserializeArray(data);
                    } else if (ch == 's'){
                        val = getStringVal(data);
                    } else if (ch == 'i') {
                        val = getIntegerVal(data);
                    } else if (ch == 'N') {
                        index++;
                    } else if (ch == 'b') {
                        val = getIntegerVal(data);
                        val = new Boolean((Integer)val == 1);
                    } else if (ch == 'd') {
                        val = getFloatVal(data);
                    } else if (ch == 'r' || ch == 'R') {
                        val = getIntegerVal(data);
                        val = new PropertiesReference((Integer)val - 2, key);
                        referencesList.add((PropertiesReference)val);
                    }
                    objectProperties.put(key, val);
                    readingVal = false;
                    readingKey = true;
                }
            } else if (ch == '{') {
                inObject = true;
                readingKey = true;
            }

            index++;
            if (index >= data.length()) {
                break;
            }
        }

        for(PropertiesReference ref : referencesList) {
            int refNum = ref.getRefNum();
            objectProperties.put(ref.getKey(), objectProperties.get(keysList.get(refNum)));
        }

        return objectProperties;
    }

    private Object unserializeArray(String data) throws Exception.POJORequiresEmptyConstructorException {
        return getObjectProperties(data);
    }

    private Object unserializeObject(String data) throws Exception.POJORequiresEmptyConstructorException {
        String objectType = getObjectType(data);
        LinkedHashMap<String, Object> objectProperties = getObjectProperties(data);
        return createObject(objectType, objectProperties);
    }

    private <T>Object createObject(String type, LinkedHashMap<String, Object> objectProperties) throws Exception.POJORequiresEmptyConstructorException {

        Object o = null;
        for (Class<?> aClass : classes) {
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
            if (phpClass.equals(type)) {
                try {
                    o = aClass.newInstance();
                    break;
                } catch (InstantiationException e) {
                    throw new Exception.POJORequiresEmptyConstructorException(aClass.getName());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        Class<?> oClass = o.getClass();

        for(String propertyName : objectProperties.keySet()) {
            Object propertyValue = objectProperties.get(propertyName);
            if (propertyValue == null) continue;

            try {
                Field field = oClass.getDeclaredField(propertyName);
                field.setAccessible(true);
                Type fieldType = field.getGenericType();
                Class<? extends Type> fieldTypeClass = fieldType.getClass();

                if (propertyValue instanceof Double && fieldTypeClass.isInstance(Float.class)) {
                    propertyValue = ((Double) propertyValue).floatValue();
                } else if (propertyValue instanceof Float && fieldTypeClass.isInstance(Double.class)){
                    propertyValue = ((Float) propertyValue).doubleValue();
                }
                field.set(o, propertyValue);

            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        return o;
    }
}
