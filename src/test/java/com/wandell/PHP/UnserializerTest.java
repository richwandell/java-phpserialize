package com.wandell.PHP;

import com.wandell.PHP.Exception.MissingPHPClassAnnotation;
import com.wandell.PHP.pojo.Pojo2;
import org.junit.jupiter.api.Test;
import static com.wandell.PHP.PHP.*;
import static org.junit.jupiter.api.Assertions.*;
import com.wandell.PHP.pojo.Pojo1;
import java.util.LinkedHashMap;

public class UnserializerTest {
    @Test
    void testArrayWithReference() {
        try {
            Unserializer u = new Unserializer();
            LinkedHashMap array = (LinkedHashMap<String, Object>) u.unserialize(ARRAY_REFERENCE);
            assertSame(array.get("0"), array.get("1"));
        } catch (java.lang.Exception e) {
            fail();
        }
    }

    @Test
    void testNestedMoreThanOneClassWithReference() {
        try {
            Unserializer u = new Unserializer(TEST_CLASSES);
            Pojo2 o = (Pojo2)u.unserialize(OBJECT_NESTED_MULTIPLE_TYPES);

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
            Pojo1 o = (Pojo1)u.unserialize(OBJECT_NESTED);

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
            LinkedHashMap<String, Pojo1> array = (LinkedHashMap<String, Pojo1>) u.unserialize(ARRAY_NESTED_OBJECTS);

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
            LinkedHashMap<String, Integer> array = (LinkedHashMap<String, Integer>) u.unserialize(ARRAY_NUMBERS);

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