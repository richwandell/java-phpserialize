package com.wandell.PHP;

public class Exception {
    public static class MissingPHPClassAnnotation extends java.lang.Exception {

        public MissingPHPClassAnnotation(String className) {
            super(className + " must implement PHPClass interface.");
        }
    }

    public static class POJORequiresEmptyConstructorException extends java.lang.Exception {
        public POJORequiresEmptyConstructorException(String className) {
            super(className + " must have an empty default constructor.");
        }
    }
}
