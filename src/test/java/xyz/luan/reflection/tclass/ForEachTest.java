package xyz.luan.reflection.tclass;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class ForEachTest {

	private Integer[] myIntsArray = new Integer[] { 1, 2, 3 };
	private List<Integer> myInts = Arrays.asList(2, 3, 4);
	private Map<Integer, String> myMap = generateMyMap();

	private static Map<Integer, String> generateMyMap() {
		Map<Integer, String> myMap = new HashMap<>();
		myMap.put(3, "three");
		myMap.put(5, "five");
		myMap.put(7, "seven");
		return myMap;
	}

	@Test
	public void testArrayForEach() {
		final List<Object> results = new ArrayList<>();
		TypedClass.create(field("myIntsArray")).asList().forEach(myIntsArray, (el) -> results.add(el));
		Assert.assertArrayEquals(myIntsArray, results.toArray());
	}

	@Test
	public void testListForEach() {
		final List<Object> results = new ArrayList<>();
		TypedClass.create(field("myInts")).asList().forEach(myInts, (el) -> results.add(el));
		Assert.assertArrayEquals(myInts.toArray(), results.toArray());
	}

	@Test
	public void testMapKeyForEach() {
		final List<Object> results = new ArrayList<>();
		TypedClass.create(field("myMap")).asMap().forEachKey(myMap, (el) -> results.add(el));
		Assert.assertArrayEquals(myMap.keySet().toArray(), results.toArray());
	}

	@Test
	public void testMapValueForEach() {
		final List<Object> results = new ArrayList<>();
		TypedClass.create(field("myMap")).asMap().forEachValue(myMap, (el) -> results.add(el));
		Assert.assertArrayEquals(myMap.values().toArray(), results.toArray());
	}

	@Test
	public void testMapForEach() {
		final Map<Object, Object> results = new HashMap<>();
		TypedClass.create(field("myMap")).asMap().forEach(myMap, results::put);
		Assert.assertEquals(myMap.toString(), results.toString());
	}

	private Field field(String name) {
		try {
			return this.getClass().getDeclaredField(name);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
}
