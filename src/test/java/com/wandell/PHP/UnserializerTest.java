package com.wandell.PHP;

import com.wandell.PHP.Exception.MissingPHPClassAnnotation;
import com.wandell.PHP.pojo.Pojo2;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.wandell.PHP.pojo.Pojo1;

import java.util.LinkedHashMap;

public class UnserializerTest {

    private static final Class[] TEST_CLASSES = new Class[]{
            Pojo1.class, Pojo2.class
    };

    private static final String PHP_ARRAY_NUMBERS = "a:3:{i:0;i:1;i:1;i:2;i:2;i:3;}";
    private static final String PHP_ARRAY_REFERENCE = "a:4:{s:3:\"foo\";i:4;s:3:\"bar\";i:2;i:0;s:2:\"hi\";i:1;R:4;}";
    private static final String NESTED_PHP_OBJECT = "O:26:\"wandell\\Example\\Popo\\Popo1\":7:{s:1:\"a\";O:26:\"wandell\\Example\\Popo\\Popo1\":7:{s:1:\"a\";N;s:1:\"b\";N;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000c\";s:5:\"there\";s:4:\"\u0000*\u0000d\";i:2;s:1:\"e\";i:0;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000f\";d:1.2;s:4:\"\u0000*\u0000g\";b:0;}s:1:\"b\";N;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000c\";s:2:\"hi\";s:4:\"\u0000*\u0000d\";i:1;s:1:\"e\";i:0;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000f\";d:1.2;s:4:\"\u0000*\u0000g\";b:0;}";
    private static final String PHP_ARRAY_NESTED_OBJECTS = "a:3:{i:0;O:26:\"wandell\\Example\\Popo\\Popo1\":7:{s:1:\"a\";N;s:1:\"b\";N;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000c\";s:2:\"hi\";s:4:\"\u0000*\u0000d\";i:1;s:1:\"e\";i:0;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000f\";d:1.2;s:4:\"\u0000*\u0000g\";b:0;}i:1;O:26:\"wandell\\Example\\Popo\\Popo1\":7:{s:1:\"a\";N;s:1:\"b\";N;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000c\";s:2:\"hi\";s:4:\"\u0000*\u0000d\";i:2;s:1:\"e\";i:0;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000f\";d:1.2;s:4:\"\u0000*\u0000g\";b:0;}i:2;O:26:\"wandell\\Example\\Popo\\Popo1\":7:{s:1:\"a\";N;s:1:\"b\";N;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000c\";s:2:\"hi\";s:4:\"\u0000*\u0000d\";i:3;s:1:\"e\";i:0;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000f\";d:1.2;s:4:\"\u0000*\u0000g\";b:0;}}";
    private static final String NESTED_PHP_OBJECT_MULTIPLE_TYPES = "O:26:\"wandell\\Example\\Popo\\Popo2\":2:{s:35:\"\u0000wandell\\Example\\Popo\\Popo2\u0000object1\";O:26:\"wandell\\Example\\Popo\\Popo1\":7:{s:1:\"a\";O:26:\"wandell\\Example\\Popo\\Popo1\":7:{s:1:\"a\";N;s:1:\"b\";N;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000c\";s:5:\"there\";s:4:\"\u0000*\u0000d\";i:2;s:1:\"e\";i:0;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000f\";d:1.2;s:4:\"\u0000*\u0000g\";b:0;}s:1:\"b\";N;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000c\";s:2:\"hi\";s:4:\"\u0000*\u0000d\";i:1;s:1:\"e\";i:0;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000f\";d:1.2;s:4:\"\u0000*\u0000g\";b:0;}s:35:\"\u0000wandell\\Example\\Popo\\Popo2\u0000object2\";r:2;}";

    @Test
    void testArrayWithReference() {
        try {
            Unserializer u = new Unserializer();
            LinkedHashMap array = (LinkedHashMap<String, Object>) u.unserialize(PHP_ARRAY_REFERENCE);

            assertSame(array.get("0"), array.get("1"));

        } catch (java.lang.Exception e) {
            fail();
        }
    }

    @Test
    void testNestedMoreThanOneClassWithReference() {
        try {
            Unserializer u = new Unserializer(TEST_CLASSES);
            Pojo2 o = (Pojo2)u.unserialize(NESTED_PHP_OBJECT_MULTIPLE_TYPES);

            assertSame(o.getClass(), Pojo2.class);
            assertSame(o.getObject1(), o.getObject2());
            assertSame(o.getObject1().getClass(), Pojo1.class);
            assertSame(o.getObject2().getClass(), Pojo1.class);

        } catch (java.lang.Exception e) {
            fail();
        }
    }


    @Test
    void testRequiresPHPClassAnnotation() {
        try {
            Unserializer u = new Unserializer(new Class[]{Unserializer.class});
            fail();
        } catch (MissingPHPClassAnnotation missingPHPClassAnnotation) {
            assertTrue(true);
        }
    }

    @Test
    void testUnserializeNestedObject() {
        try {
            Unserializer u = new Unserializer(TEST_CLASSES);
            Pojo1 o = (Pojo1)u.unserialize(NESTED_PHP_OBJECT);

            assertSame(o.getClass(), Pojo1.class);
            assertEquals(o.getC(), "hi");
            assertSame(o.getD(), 1);
            assertSame(o.e, 0);
            assertSame(o.getF().getClass(), Double.class);

            assertSame(o.a.getClass(), Pojo1.class);
            assertEquals(o.a.getC(), "there");
            assertSame(o.a.getD(), 2);
            assertSame(o.a.e, 0);
            assertSame(o.a.getF().getClass(), Double.class);

        } catch (java.lang.Exception e) {
            fail();
        }
    }

    @Test
    void testUnserializeArrayOfObjects() {
        try {
            Unserializer u = new Unserializer(TEST_CLASSES);
            LinkedHashMap<String, Pojo1> array = (LinkedHashMap<String, Pojo1>) u.unserialize(PHP_ARRAY_NESTED_OBJECTS);

            int itemId = 1;
            for(Pojo1 item : array.values()) {
                assertSame(item.getD(), itemId);
                itemId++;
            }
        } catch (java.lang.Exception e) {
            fail();
        }
    }

    @Test
    void testUnserializeArrayNumbers() {
        try {
            Unserializer u = new Unserializer(TEST_CLASSES);
            LinkedHashMap<String, Integer> array = (LinkedHashMap<String, Integer>) u.unserialize(PHP_ARRAY_NUMBERS);

            int itemId = 1;
            for(int item : array.values()) {
                assertEquals(item, itemId);
                itemId++;
            }
        } catch (java.lang.Exception e) {
            fail();
        }
    }
}