package com.wandell.PHP;

import com.wandell.PHP.pojo.Pojo1;
import com.wandell.PHP.pojo.Pojo2;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SerializerTest {

    private static final String SIMPLE_OBJECT = "O:26:\"wandell\\Example\\Popo\\Popo1\":7:{s:1:\"a\";N;s:1:\"b\";i:0;s:29:\"\0wandell\\Example\\Popo\\Popo1\0c\";N;s:4:\"\0*\0d\";i:0;s:1:\"e\";N;s:29:\"\0wandell\\Example\\Popo\\Popo1\0f\";N;s:4:\"\0*\0g\";b:0;}";
    private static final String OBJECT_WITH_REFERENCE = "O:26:\"wandell\\Example\\Popo\\Popo2\":2:{s:35:\"\0wandell\\Example\\Popo\\Popo2\0object1\";O:26:\"wandell\\Example\\Popo\\Popo1\":7:{s:1:\"a\";N;s:1:\"b\";i:0;s:29:\"\0wandell\\Example\\Popo\\Popo1\0c\";N;s:4:\"\0*\0d\";i:0;s:1:\"e\";N;s:29:\"\0wandell\\Example\\Popo\\Popo1\0f\";N;s:4:\"\0*\0g\";b:0;}s:35:\"\0wandell\\Example\\Popo\\Popo2\0object2\";r:2;}";

    @Test
    void testSimpleSerialize() {
        Pojo1 pojo1 = new Pojo1();

        try {
            Serializer serializer = new Serializer();
            String serialized = serializer.serialize(pojo1);

            assertEquals(serialized, SIMPLE_OBJECT);
        } catch (Exception.MissingPHPClassAnnotation missingPHPClassAnnotation) {
            fail();
        }
    }

    @Test
    void testSerializeObjectWithReferences() {
        Pojo1 pojo1 = new Pojo1();
        Pojo2 pojo2 = new Pojo2(pojo1, pojo1);


        try {
            Serializer serializer = new Serializer();
            String serialized = serializer.serialize(pojo2);

            assertEquals(serialized, OBJECT_WITH_REFERENCE);
        } catch (Exception.MissingPHPClassAnnotation missingPHPClassAnnotation) {
            fail();
        }
    }
}
