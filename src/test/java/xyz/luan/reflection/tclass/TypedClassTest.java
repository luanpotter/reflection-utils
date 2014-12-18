package xyz.luan.reflection.tclass;

import java.lang.annotation.Annotation;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import xyz.luan.reflection.entities.Car;
import xyz.luan.reflection.entities.ComplexModel;
import xyz.luan.reflection.entities.ComplexModel.MyAnnotation;

public class TypedClassTest {

    @Test
    public void testSimpleField() throws NoSuchFieldException, SecurityException {
        TypedClass<?> mapClass = TypedClass.create(Car.class.getDeclaredField("preBuiltModels"));
        assertMap(mapClass);
        Assert.assertEquals(String.class, mapClass.asMap().getKey().asClass());
        Assert.assertEquals(Car.class, mapClass.asMap().getValue().asClass());
    }

    @Test
    public void testIntemediateField() throws NoSuchFieldException, SecurityException {
        TypedClass<?> arrayOfListOfStrings = TypedClass.create(ComplexModel.class.getDeclaredField("arrayOfListOfString"));

        assertList(arrayOfListOfStrings);
        assertLevel(arrayOfListOfStrings.getAnnotations(), "list[]");

        TypedClass<?> listOfString = arrayOfListOfStrings.asList().getComponent();
        assertList(listOfString);
        assertLevel(listOfString.getAnnotations(), "list");

        TypedClass<?> string = listOfString.asList().getComponent();
        assertNone(string);
        assertLevel(string.getAnnotations(), "string");
    }

    @Test
    public void testComplexField() throws NoSuchFieldException, SecurityException {
        TypedClass<?> arrayOfListOfMapOf_ArrayOfString_to_ArrayOfInt = TypedClass.create(ComplexModel.class.getDeclaredField("arrayOfListOfMapOf_ArrayOfString_to_ArrayOfInt"));
        _testComplexField(arrayOfListOfMapOf_ArrayOfString_to_ArrayOfInt);
    }

    @Test
    public void testComplexFieldWithSubtypes() throws NoSuchFieldException, SecurityException {
        TypedClass<?> arrayOfListOfMapOf_ArrayOfString_to_ArrayOfInt_withSubtypes = TypedClass.create(ComplexModel.class.getDeclaredField("arrayOfListOfMapOf_ArrayOfString_to_ArrayOfInt_withSubtypes"));
        _testComplexField(arrayOfListOfMapOf_ArrayOfString_to_ArrayOfInt_withSubtypes);
    }

    private void _testComplexField(TypedClass<?> arrayOfListOfMapOf_ArrayOfString_to_ArrayOfInt) {
        assertList(arrayOfListOfMapOf_ArrayOfString_to_ArrayOfInt);
        assertLevel(arrayOfListOfMapOf_ArrayOfString_to_ArrayOfInt.getAnnotations(), "list[]");

        TypedClass<?> listOfMapOf_ArrayOfString_to_ArrayOfInt = arrayOfListOfMapOf_ArrayOfString_to_ArrayOfInt.asList().getComponent();
        assertList(listOfMapOf_ArrayOfString_to_ArrayOfInt);
        assertLevel(listOfMapOf_ArrayOfString_to_ArrayOfInt.getAnnotations(), "list");

        TypedClass<?> mapOf_ArrayOfString_to_ArrayOfInt = listOfMapOf_ArrayOfString_to_ArrayOfInt.asList().getComponent();
        assertMap(mapOf_ArrayOfString_to_ArrayOfInt);
        assertLevel(mapOf_ArrayOfString_to_ArrayOfInt.getAnnotations(), "map");

        {
            TypedClass<?> arrayOfString = mapOf_ArrayOfString_to_ArrayOfInt.asMap().getKey();
            assertList(arrayOfString);
            assertLevel(arrayOfString.getAnnotations(), "string[]");

            TypedClass<?> string = arrayOfString.asList().getComponent();
            assertNone(string);
            assertLevel(string.getAnnotations(), "string");
        }

        {
            TypedClass<?> arrayOfInt = mapOf_ArrayOfString_to_ArrayOfInt.asMap().getValue();
            assertList(arrayOfInt);
            assertLevel(arrayOfInt.getAnnotations(), "int[]");

            TypedClass<?> integer = arrayOfInt.asList().getComponent();
            assertNone(integer);
            assertLevel(integer.getAnnotations(), "int");
        }
    }

    private void assertNone(TypedClass<?> t) {
        Assert.assertFalse(t.isMap());
        Assert.assertFalse(t.isList());
    }

    private void assertList(TypedClass<?> t) {
        Assert.assertFalse(t.isMap());
        Assert.assertTrue(t.isList());
    }

    private void assertMap(TypedClass<?> t) {
        Assert.assertFalse(t.isList());
        Assert.assertTrue(t.isMap());
    }

    private void assertLevel(List<Annotation> annotations, String string) {
        Assert.assertEquals(1, annotations.size());
        MyAnnotation a = (MyAnnotation) annotations.get(0);
        Assert.assertEquals(string, a.value());
    }
}
