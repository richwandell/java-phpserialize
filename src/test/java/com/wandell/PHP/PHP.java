package com.wandell.PHP;

import com.wandell.PHP.pojo.Pojo1;
import com.wandell.PHP.pojo.Pojo2;

public class PHP {
    static final Class<?>[] TEST_CLASSES = new Class[]{
            Pojo1.class, Pojo2.class
    };
    static final String ARRAY_NUMBERS = "a:3:{i:0;i:1;i:1;i:2;i:2;i:3;}";
    static final String ARRAY_REFERENCE = "a:4:{s:3:\"foo\";i:4;s:3:\"bar\";i:2;i:0;s:2:\"hi\";i:1;R:4;}";
    static final String ARRAY_NESTED_OBJECTS = "a:3:{i:0;O:26:\"wandell\\Example\\Popo\\Popo1\":7:{s:1:\"a\";N;s:1:\"b\";N;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000c\";s:2:\"hi\";s:4:\"\u0000*\u0000d\";i:1;s:1:\"e\";i:0;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000f\";d:1.2;s:4:\"\u0000*\u0000g\";b:0;}i:1;O:26:\"wandell\\Example\\Popo\\Popo1\":7:{s:1:\"a\";N;s:1:\"b\";N;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000c\";s:2:\"hi\";s:4:\"\u0000*\u0000d\";i:2;s:1:\"e\";i:0;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000f\";d:1.2;s:4:\"\u0000*\u0000g\";b:0;}i:2;O:26:\"wandell\\Example\\Popo\\Popo1\":7:{s:1:\"a\";N;s:1:\"b\";N;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000c\";s:2:\"hi\";s:4:\"\u0000*\u0000d\";i:3;s:1:\"e\";i:0;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000f\";d:1.2;s:4:\"\u0000*\u0000g\";b:0;}}";
    static final String OBJECT_SIMPLE = "O:26:\"wandell\\Example\\Popo\\Popo1\":7:{s:1:\"a\";N;s:1:\"b\";i:0;s:29:\"\0wandell\\Example\\Popo\\Popo1\0c\";N;s:4:\"\0*\0d\";i:0;s:1:\"e\";N;s:29:\"\0wandell\\Example\\Popo\\Popo1\0f\";N;s:4:\"\0*\0g\";b:0;}";
    static final String OBJECT_NESTED = "O:26:\"wandell\\Example\\Popo\\Popo1\":7:{s:1:\"a\";O:26:\"wandell\\Example\\Popo\\Popo1\":7:{s:1:\"a\";N;s:1:\"b\";N;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000c\";s:5:\"there\";s:4:\"\u0000*\u0000d\";i:2;s:1:\"e\";i:0;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000f\";d:1.2;s:4:\"\u0000*\u0000g\";b:0;}s:1:\"b\";N;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000c\";s:2:\"hi\";s:4:\"\u0000*\u0000d\";i:1;s:1:\"e\";i:0;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000f\";d:1.2;s:4:\"\u0000*\u0000g\";b:0;}";
    static final String OBJECT_NESTED_MULTIPLE_TYPES = "O:26:\"wandell\\Example\\Popo\\Popo2\":2:{s:35:\"\u0000wandell\\Example\\Popo\\Popo2\u0000object1\";O:26:\"wandell\\Example\\Popo\\Popo1\":7:{s:1:\"a\";O:26:\"wandell\\Example\\Popo\\Popo1\":7:{s:1:\"a\";N;s:1:\"b\";N;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000c\";s:5:\"there\";s:4:\"\u0000*\u0000d\";i:2;s:1:\"e\";i:0;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000f\";d:1.2;s:4:\"\u0000*\u0000g\";b:0;}s:1:\"b\";N;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000c\";s:2:\"hi\";s:4:\"\u0000*\u0000d\";i:1;s:1:\"e\";i:0;s:29:\"\u0000wandell\\Example\\Popo\\Popo1\u0000f\";d:1.2;s:4:\"\u0000*\u0000g\";b:0;}s:35:\"\u0000wandell\\Example\\Popo\\Popo2\u0000object2\";r:2;}";
    static final String OBJECT_WITH_REFERENCE = "O:26:\"wandell\\Example\\Popo\\Popo2\":2:{s:35:\"\0wandell\\Example\\Popo\\Popo2\0object1\";O:26:\"wandell\\Example\\Popo\\Popo1\":7:{s:1:\"a\";N;s:1:\"b\";i:0;s:29:\"\0wandell\\Example\\Popo\\Popo1\0c\";N;s:4:\"\0*\0d\";i:0;s:1:\"e\";N;s:29:\"\0wandell\\Example\\Popo\\Popo1\0f\";N;s:4:\"\0*\0g\";b:0;}s:35:\"\0wandell\\Example\\Popo\\Popo2\0object2\";r:2;}";
}
