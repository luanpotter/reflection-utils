package xyz.luan.reflection.tclass;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;

class Helper {

	static TypedClass<?> create(Field f) {
		return create(r().resolve(f.getGenericType()), f.getAnnotatedType());
	}

	static TypedClass<?> create(Class<?> clazz) {
		ResolvedType type = r().resolve(clazz);

		if (type.isInstanceOf(Collection.class)) {
			return create(type, getAnnotatedSuperInterface(clazz, Collection.class));
		} else if (type.isInstanceOf(Map.class)) {
			return create(type, getAnnotatedSuperInterface(clazz, Map.class));
		}
		return create(type, new EmptyAnnotationType());
	}

	private static AnnotatedType getAnnotatedSuperInterface(Class<?> clazz, Class<?> target) {
		for (AnnotatedType t : clazz.getAnnotatedInterfaces()) {
			if (target.isAssignableFrom(r().resolve(t.getType()).getErasedType())) {
				return t;
			}
		}
		return new EmptyAnnotationType();
	}

	private static TypeResolver r() {
		return new TypeResolver();
	}

	public static class EmptyAnnotationType implements AnnotatedArrayType, AnnotatedParameterizedType {

		@Override
		public Type getType() {
			return null;
		}

		@Override public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
			return false;
		}

		@Override
		public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
			return null;
		}

		@Override
		public Annotation[] getAnnotations() {
			return new Annotation[0];
		}

		@Override public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
			return null;
		}

		@Override public <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass) {
			return null;
		}

		@Override public <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> annotationClass) {
			return null;
		}

		@Override
		public Annotation[] getDeclaredAnnotations() {
			return new Annotation[0];
		}

		@Override
		public AnnotatedType[] getAnnotatedActualTypeArguments() {
			return new AnnotatedType[] { new EmptyAnnotationType(), new EmptyAnnotationType() };
		}

		@Override
		public AnnotatedType getAnnotatedGenericComponentType() {
			return new EmptyAnnotationType();
		}

		// for java 9+
		public AnnotatedType getAnnotatedOwnerType() {
			return null;
		}
	}

	static TypedClass<?> create(ResolvedType type, AnnotatedType at) {
		if (at == null) {
			return new TypedClass<>(type.getErasedType(), type, null, new Annotation[0]);
		}
		Annotation[] annotations = at.getAnnotations();
		if (type.isArray()) {
			AnnotatedType childAnnotatedType = ((AnnotatedArrayType) at).getAnnotatedGenericComponentType();
			ResolvedType childType = type.getArrayElementType();
			return new ListClass<>(type.getErasedType(), type, at, annotations, create(childType, childAnnotatedType));
		} else if (type.isInstanceOf(Collection.class)) {
			List<TypedClass<?>> results = getGenericParameters(at, type, List.class, 1);
			return new ListClass<>(type.getErasedType(), type, at, annotations, results.get(0));
		} else if (type.isInstanceOf(Map.class)) {
			List<TypedClass<?>> results = getGenericParameters(at, type, Map.class, 2);
			return new MapClass<>(type.getErasedType(), type, at, annotations, results.get(0), results.get(1));
		}
		return new TypedClass<>(type.getErasedType(), type, at, annotations);
	}

	static List<TypedClass<?>> getGenericParameters(AnnotatedType annotatedType, ResolvedType type, Class<?> targetClass, int amount) {
		/*
		 * TODO(luan) if more generic parameters are added, this wont work;
		 * something like type.annotatedParametersFor must be used
		 */
		if (!(annotatedType instanceof AnnotatedParameterizedType)) {
			return Collections.emptyList();
		}

		AnnotatedType[] actualArguments = ((AnnotatedParameterizedType) annotatedType).getAnnotatedActualTypeArguments();
		List<ResolvedType> types = Helper.getParameters(type, targetClass, amount);

		List<TypedClass<?>> results = new ArrayList<>(types.size());
		for (int i = 0; i < amount; i++) {
			results.add(Helper.create(types.get(i), actualArguments[i]));
		}
		return results;
	}

	static List<ResolvedType> getParameters(ResolvedType type, Class<?> target, int amount) {
		List<ResolvedType> types = type.typeParametersFor(target);
		if (types == null || types.isEmpty()) {
			return fillObjects(amount);
		}
		return types;
	}

	static List<ResolvedType> fillObjects(int amount) {
		return Collections.nCopies(amount, r().resolve(Object.class));
	}
}
