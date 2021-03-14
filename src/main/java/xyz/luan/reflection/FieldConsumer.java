package xyz.luan.reflection;

import java.lang.reflect.Field;

@FunctionalInterface
public interface FieldConsumer {

	void accept(Field t) throws IllegalAccessException;
}
