package xyz.luan.reflection.tclass;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;

class Helper {

    static TypedClass<?> create(Field f) {
        final TypeResolver typeResolver = new TypeResolver();
        ResolvedType type = typeResolver.resolve(f.getGenericType());
        return create(type, f.getAnnotatedType());
    }

    private static TypedClass<?> create(ResolvedType type, AnnotatedType at) {
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
            ResolvedType childType = type.typeParametersFor(Collection.class).get(0);
            return new ListClass<>(type.getErasedType(), annotations, create(childType, childAnnotatedType));
        } else if (type.isInstanceOf(Map.class)) {
            AnnotatedType[] att = ((AnnotatedParameterizedType) at).getAnnotatedActualTypeArguments();
            List<ResolvedType> types = type.typeParametersFor(Map.class);
            TypedClass<?> key = create(types.get(0), att[0]);
            TypedClass<?> value = create(types.get(1), att[1]);
            return new MapClass<>(type.getErasedType(), annotations, key, value);
        }
        return new TypedClass<>(type.getErasedType(), annotations);
    }
}
