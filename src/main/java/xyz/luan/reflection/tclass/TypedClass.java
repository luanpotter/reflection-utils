package xyz.luan.reflection.tclass;

import com.fasterxml.classmate.ResolvedType;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import xyz.luan.reflection.FieldConsumer;
import xyz.luan.reflection.ReflectionUtils;

import static java.util.stream.Collectors.toList;

/**
 * This represents a Class but fully qualified with information about: generic parameters and annotations.
 * In order to get an instance of this, the easiest way is to call `TypedClass.create(field)` with a field
 * object. Field objects contain all the extra information that a plain Class instance lacks due to type
 * erasure.
 * @param <T> the class this Class points to.
 */
public class TypedClass<T> {

	protected Class<T> ref;
	protected ResolvedType type;
	protected AnnotatedType annotatedType;
	protected List<Annotation> annotations;

	TypedClass(Class<T> ref, ResolvedType type, AnnotatedType annotatedType, Annotation[] annotations) {
		this.ref = ref;
		this.type = type;
		this.annotatedType = annotatedType;
		this.annotations = Arrays.asList(annotations);
	}

	/**
	 * Returns the raw Class this TypedClass points to.
	 * @return the original Class
	 */
	public Class<T> asClass() {
		return this.ref;
	}

	public boolean isSubtypeOf(Class<?> c) {
		return c.isAssignableFrom(ref);
	}

	public boolean isSubtypeOf(TypedClass<?> c) {
		return isSubtypeOf(c.asClass());
	}

	public String getName() {
		return this.ref.getCanonicalName();
	}

	public boolean isList() {
		return false;
	}

	public boolean isMap() {
		return false;
	}

	public ListClass<T> asList() {
		return (ListClass<T>) this;
	}

	public MapClass<T> asMap() {
		return (MapClass<T>) this;
	}

	public List<TypedClass<?>> getGenericParameters(Class<?> targetClass, int amount) {
		return Helper.getGenericParameters(annotatedType, type, targetClass, amount);
	}

	public List<Annotation> getAnnotations() {
		return this.annotations;
	}

	public static <T> TypedClass<T> create(Class<T> clazz) {
		return (TypedClass<T>) Helper.create(clazz);
	}

	public static TypedClass<?> create(Field f) {
		return Helper.create(f);
	}

	public List<Field> fields() {
		return ReflectionUtils.getFieldsRecursively(ref);
	}

	public void fields(FieldConsumer consumer) {
		ReflectionUtils.consumeFieldsRecursively(ref, consumer);
	}

	@Override
	public String toString() {
		return this.ref.toString();
	}
}
