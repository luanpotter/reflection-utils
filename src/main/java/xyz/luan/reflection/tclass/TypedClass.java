package xyz.luan.reflection.tclass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import xyz.luan.reflection.FieldConsumer;
import xyz.luan.reflection.ReflectionUtils;

public class TypedClass<T> {

	protected Class<T> ref;
	protected List<Annotation> annotations;

	TypedClass(Class<T> ref, Annotation[] annotations) {
		this.ref = ref;
		this.annotations = Arrays.asList(annotations);
	}

	public Class<T> asClass() {
		return this.ref;
	}

	public boolean isSubtypeOf(Class<?> c) {
		return this.ref.isAssignableFrom(c);
	}

	public boolean isSubtypeOf(TypedClass<?> c) {
		return isSubtypeOf(c.getClass());
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

	public List<Annotation> getAnnotations() {
		return this.annotations;
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
