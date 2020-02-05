package com.wandell.PHP;

import com.wandell.PHP.pojo.Pojo1;
import com.wandell.PHP.pojo.Pojo2;
import org.junit.jupiter.api.Test;

import static com.wandell.PHP.PHP.*;
import static org.junit.jupiter.api.Assertions.*;

public class SerializerTest {
    @Test
    void testSimpleSerialize() {
        Pojo1 pojo1 = new Pojo1();

        try {
            Serializer serializer = new Serializer();
            String serialized = serializer.serialize(pojo1);

            assertEquals(serialized, OBJECT_SIMPLE);
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
