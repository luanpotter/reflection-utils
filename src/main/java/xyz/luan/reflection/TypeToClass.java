package xyz.luan.reflection;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class TypeToClass {

    public static class GenericTypeException extends RuntimeException {
        private static final long serialVersionUID = 3581525619808088968L;
    }

    private TypeToClass() {
        throw new RuntimeException("Should not be instanciated");
    }

    public static Class<?> convert(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }

        if (type instanceof ParameterizedType) {
            return convert(((ParameterizedType) type).getRawType());
        }

        if (type instanceof GenericArrayType) {
            try {
                Class<?> component = convert(((GenericArrayType) type).getGenericComponentType());
                return Class.forName("[L" + component.getCanonicalName() + ";");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("This should never happen!", e);
            }
        }

        throw new GenericTypeException();
    }
}
