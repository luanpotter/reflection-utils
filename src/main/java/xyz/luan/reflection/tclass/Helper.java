package xyz.luan.reflection.tclass;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
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
		final TypeResolver typeResolver = new TypeResolver();
		return typeResolver;
	}

	public static class EmptyAnnotationType implements AnnotatedArrayType, AnnotatedParameterizedType {

		@Override
		public Type getType() {
			return null;
		}

		@Override
		public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
			return null;
		}

		@Override
		public Annotation[] getAnnotations() {
			return new Annotation[0];
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

	}

	private static TypedClass<?> create(ResolvedType type, AnnotatedType at) {
		if (at == null) {
			return new TypedClass<>(type.getErasedType(), new Annotation[0]);
		}
		Annotation[] annotations = at.getAnnotations();
		if (type.isArray()) {
			AnnotatedType childAnnotatedType = ((AnnotatedArrayType) at).getAnnotatedGenericComponentType();
			ResolvedType childType = type.getArrayElementType();
			return new ListClass<>(type.getErasedType(), annotations, create(childType, childAnnotatedType));
		} else if (type.isInstanceOf(Collection.class)) {
			/*
			 * TODO if more generic parameters are added, this wont work;
			 * something like type.annotatedParametersFor must be used
			 */
			AnnotatedType childAnnotatedType = ((AnnotatedParameterizedType) at).getAnnotatedActualTypeArguments()[0];
			List<ResolvedType> types = getParameters(type, Collection.class, 1);
			ResolvedType childType = types.get(0);
			return new ListClass<>(type.getErasedType(), annotations, create(childType, childAnnotatedType));
		} else if (type.isInstanceOf(Map.class)) {
			AnnotatedType[] att = ((AnnotatedParameterizedType) at).getAnnotatedActualTypeArguments();
			List<ResolvedType> types = getParameters(type, Map.class, 2);
			TypedClass<?> key = create(types.get(0), att[0]);
			TypedClass<?> value = create(types.get(1), att[1]);
			return new MapClass<>(type.getErasedType(), annotations, key, value);
		}
		return new TypedClass<>(type.getErasedType(), annotations);
	}

	private static List<ResolvedType> getParameters(ResolvedType type, Class<?> target, int amount) {
		List<ResolvedType> types = type.typeParametersFor(target);
		if (types == null || types.isEmpty()) {
			return fillObjects(amount);
		}
		return types;
	}

	private static List<ResolvedType> fillObjects(int amount) {
		List<ResolvedType> types = new ArrayList<>();
		for (int i = 0; i < amount; i++) {
			types.add(r().resolve(Object.class));
		}
		return types;
	}
}
