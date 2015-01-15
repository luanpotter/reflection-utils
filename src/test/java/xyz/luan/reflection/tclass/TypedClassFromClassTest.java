package xyz.luan.reflection.tclass;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import xyz.luan.reflection.entities.ComplexModel.MyAnnotation;

public class TypedClassFromClassTest {

	@Test
	public void testSimpleString() {
		TypedClass<String> clazz = TypedClass.create(String.class);
		Assert.assertFalse(clazz.isList());
		Assert.assertFalse(clazz.isMap());
		Assert.assertEquals(String.class, clazz.asClass());
	}

	@Test
	public void testList() {
		TypedClass<?> clazz = TypedClass.create(List.class);
		Assert.assertTrue(clazz.isList());
		Assert.assertFalse(clazz.isMap());
		Assert.assertEquals(List.class, clazz.asClass());
	}

	@Test
	public void testMap() {
		TypedClass<?> clazz = TypedClass.create(Map.class);
		Assert.assertFalse(clazz.isList());
		Assert.assertTrue(clazz.isMap());
		Assert.assertEquals(Map.class, clazz.asClass());
	}

	@Test
	public void testInheritance() {
		TypedClass<MyList> clazz = TypedClass.create(MyList.class);
		Assert.assertTrue(clazz.isList());
		Assert.assertFalse(clazz.isMap());
		Assert.assertEquals(MyList.class, clazz.asClass());

		List<Annotation> annotations = clazz.asList().getComponent().getAnnotations();
		MyAnnotation myAnnotation = (MyAnnotation) annotations.get(0);
		Assert.assertEquals("ints", myAnnotation.value());
	}

	public abstract static class MyList implements List<@MyAnnotation("ints") Integer> {
	}
}
