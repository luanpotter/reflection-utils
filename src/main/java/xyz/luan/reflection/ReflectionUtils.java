package xyz.luan.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The utility class {@code ReflectionUtils} contains methods for performing
 * common reflexive operations not found in Java's default API.
 * 
 * @author Luan Nico
 */
public final class ReflectionUtils {

	private ReflectionUtils() {
		throw new RuntimeException("Should not be instantiated");
	}

	/**
	 * Returns whether clazz is an inner class (either anonymous, method local or non-static nested).
	 * 
	 * @param clazz
	 *            the class to be evaluated
	 * @return if clazz is an inner class or not
	 */
	public static boolean isInnerClass(Class<?> clazz) {
		return clazz.getEnclosingClass() != null && !Modifier.isStatic(clazz.getModifiers());
	}

	/**
	 * Returns all the immediate declared instance fields of a given class.
	 * This iterates through the class and selects every non-static and non-synthetic field, returning them as a {@code List} rather than an array.
	 * 
	 * @param clazz
	 *            the class to be evaluated
	 * @return a {@code List} with all the class's immediate declared instance fields
	 */
	public static List<Field> getImmediateFields(Class<?> clazz) {
		return Arrays.stream(clazz.getDeclaredFields()).filter(field -> !Modifier.isStatic(field.getModifiers()) && !field.isSynthetic())
				.collect(Collectors.toList());
	}

	/**
	 * Returns all the instance fields of a given class, going up throughout it's hierarchy until reaching any Java class.
	 * The method selects all non-static non-synthetic declared fields.
	 * It will stop going up the hierarchy if it reaches a Java class (see {@link xyz.luan.reflection.ReflectionUtils#isJavaClass isJavaClass()}).
	 * For example, the internal array inside String class will not be returned if you class extends String.
	 * 
	 * @param clazz
	 *            the class to be evaluated
	 * @return a {@code List} with all the class's fields
	 */
	public static List<Field> getFieldsRecursivelyExceptJavaClasses(Class<?> clazz) {
		return getFieldsRecursively(clazz, c -> !isJavaClass(c));
	}

	/**
	 * Returns all the instance fields of a given class, going up throughout it's hierarchy until reaching the {@code Object} class.
	 * The method selects all non-static non-synthetic declared fields.
	 * If the class used is a base class (see {@link xyz.luan.reflection.ReflectionUtils#isBaseClass isBaseClass()}) the list will be empty.
	 * 
	 * @param clazz
	 *            the class to be evaluated
	 * @return a {@code List} with all the class's fields
	 */
	public static List<Field> getFieldsRecursively(Class<?> clazz) {
		return getFieldsRecursively(clazz, c -> !isBaseClass(c));
	}

	/**
	 * Applies a given consumer to every field returned by {@link xyz.luan.reflection.ReflectionUtils#getFieldsRecursively getFieldsRecursively()}.
	 * Easy to use as it unchecks every exception thrown in the process.
	 * 
	 * @param clazz
	 *            The class to be evaluated
	 * @param consumer
	 *            The function to be run for each field
	 */
	public static void consumeFieldsRecursively(Class<?> clazz, FieldConsumer consumer) {
		getFieldsRecursively(clazz, c -> !isBaseClass(c)).forEach((f) -> {
			try {
				f.setAccessible(true);
				consumer.accept(f);
			} catch (IllegalAccessException ex) {
				throw new RuntimeException(ex);
			}
		});
	}

	private static List<Field> getFieldsRecursively(Class<?> clazz, Predicate<Class<?>> pre) {
		List<Field> fields = new ArrayList<>();
		while (pre.test(clazz)) {
			fields.addAll(ReflectionUtils.getImmediateFields(clazz));
			clazz = clazz.getSuperclass();
		}
		return fields;
	}

	/**
	 * Returns true whether clazz is a base class or not.
	 * Base classes are primitives, enums, arrays of any kind or the {@code Object} class.
	 * 
	 * @param clazz
	 *            the class to be evaluated
	 * @return whether clazz is a base class or not
	 */
	public static boolean isBaseClass(Class<?> clazz) {
		return Object.class.equals(clazz) || clazz.isPrimitive() || clazz.isEnum() || clazz.isArray();
	}

	/**
	 * Returns true whether clazz is a Java class or not.
	 * Java classes are any classes that:
	 * <ul>
	 * <li>are base classes (see {@link xyz.luan.reflection.ReflectionUtils#isBaseClass isBaseClass()})
	 * <li>belong to the packages java.* or javax* (or any of their subpackages).
	 * </ul>
	 * 
	 * @param clazz
	 *            the class to be evaluated
	 * @return whether clazz is a Java class or not
	 */
	public static boolean isJavaClass(Class<?> clazz) {
		return isBaseClass(clazz) || clazz.getPackage().getName().startsWith("java.") || clazz.getPackage().getName().startsWith("javax.");
	}

	/**
	 * Returns the Class&lt;?&gt; object for this Type.
	 * 
	 * @param type
	 *            the type object
	 * @return a Class&lt;?&gt; object associated with the type parameter
	 */
	public static Class<?> getClass(Type type) {
		return TypeToClass.convert(type);
	}
}
