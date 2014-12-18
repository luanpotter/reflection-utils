package xyz.luan.reflection.tclass;

import java.lang.annotation.Annotation;

public class MapClass<T> extends TypedClass<T> {

    private TypedClass<?> key, value;

    MapClass(Class<T> ref, Annotation[] annotations, TypedClass<?> key, TypedClass<?> value) {
        super(ref, annotations);
        this.key = key;
        this.value = value;
    }

    public TypedClass<?> getKey() {
        return key;
    }

    public TypedClass<?> getValue() {
        return value;
    }

    @Override
    public boolean isMap() {
        return true;
    }

}
