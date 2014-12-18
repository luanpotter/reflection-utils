package xyz.luan.reflection.entities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplexModel {

    private @MyAnnotation("list") ArrayList<@MyAnnotation("map") HashMap<@MyAnnotation("string") String @MyAnnotation("string[]") [], @MyAnnotation("int") int @MyAnnotation("int[]") []>> @MyAnnotation("list[]") [] arrayOfListOfMapOf_ArrayOfString_to_ArrayOfInt_withSubtypes;
    private @MyAnnotation("list") List<@MyAnnotation("map") Map<@MyAnnotation("string") String @MyAnnotation("string[]") [], @MyAnnotation("int") int @MyAnnotation("int[]") []>> @MyAnnotation("list[]") [] arrayOfListOfMapOf_ArrayOfString_to_ArrayOfInt;
    private @MyAnnotation("list") List<@MyAnnotation("string") String> @MyAnnotation("list[]") [] arrayOfListOfString;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE_USE)
    public @interface MyAnnotation {
        String value();
    }
}
