package xyz.luan.reflection.tclass;

import java.lang.annotation.Annotation;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import xyz.luan.reflection.entities.ABCHolder;
import xyz.luan.reflection.entities.Car;
import xyz.luan.reflection.entities.ComplexModel;
import xyz.luan.reflection.entities.ComplexModel.MyAnnotation;
import xyz.luan.reflection.entities.CustomParametrizedType;
import xyz.luan.reflection.entities.Id;

public class TypedClassTest {

    @Test
    public void testSimpleField() throws NoSuchFieldException, SecurityException {
        TypedClass<?> mapClass = TypedClass.create(Car.class.getDeclaredField("preBuiltModels"));
        assertMap(mapClass);
        Assert.assertEquals(String.class, mapClass.asMap().getKey().asClass());
        Assert.assertEquals(Car.class, mapClass.asMap().getValue().asClass());
    }

    @Test
    public void testIntermediateField() throws NoSuchFieldException, SecurityException {
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

    @Test
    public void testIsSubTypeOf() throws NoSuchFieldException {
        class A {}
        class B extends A {}
        class C extends B {}
        class Holder {
            A a;
            B b;
            C c;
        }

        TypedClass<?> fieldA = TypedClass.create(Holder.class.getDeclaredField("a"));
        Assert.assertTrue(fieldA.isSubtypeOf(A.class));
        Assert.assertFalse(fieldA.isSubtypeOf(B.class));
        Assert.assertFalse(fieldA.isSubtypeOf(C.class));

        TypedClass<?> fieldB = TypedClass.create(Holder.class.getDeclaredField("b"));
        Assert.assertTrue(fieldB.isSubtypeOf(A.class));
        Assert.assertTrue(fieldB.isSubtypeOf(B.class));
        Assert.assertFalse(fieldB.isSubtypeOf(C.class));

        TypedClass<?> fieldC = TypedClass.create(Holder.class.getDeclaredField("c"));
        Assert.assertTrue(fieldC.isSubtypeOf(A.class));
        Assert.assertTrue(fieldC.isSubtypeOf(B.class));
        Assert.assertTrue(fieldC.isSubtypeOf(C.class));
    }

    @Test
    public void testCustomParametrizedType() throws NoSuchFieldException {
        TypedClass<?> field = TypedClass.create(CustomParametrizedType.class.getDeclaredField("id"));
        TypedClass<?> result = field.getGenericParameters(Id.class, 1).get(0);
        Assert.assertEquals(result.asClass(), String.class);
    }

    @Test
    public void testCustomMultiParametrizedType() throws NoSuchFieldException {
        TypedClass<?> field = TypedClass.create(CustomParametrizedType.class.getDeclaredField("multiParam"));
        List<TypedClass<?>> results = field.getGenericParameters(CustomParametrizedType.MultiParam.class, 3);
        Assert.assertEquals(results.get(0).asClass(), String.class);
        Assert.assertEquals(results.get(1).asClass(), Integer.class);

        TypedClass<?> car = results.get(2).getGenericParameters(Id.class, 1).get(0);
        Assert.assertEquals(car.asClass(), Car.class);
        Assert.assertEquals(car.fields().size(), 3);
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
