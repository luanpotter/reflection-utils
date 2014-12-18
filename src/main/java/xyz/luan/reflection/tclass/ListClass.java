package xyz.luan.reflection.tclass;

import java.lang.annotation.Annotation;

public class ListClass<T> extends TypedClass<T> {

    private TypedClass<?> component;

    ListClass(Class<T> ref, Annotation[] annotations, TypedClass<?> component) {
        super(ref, annotations);
        this.component = component;
    }

    public TypedClass<?> getComponent() {
        return component;
    }

    @Override
    public boolean isList() {
        return true;
    }
}
