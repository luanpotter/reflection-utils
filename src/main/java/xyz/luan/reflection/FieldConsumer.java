package xyz.luan.reflection;

import java.lang.reflect.Field;

@FunctionalInterface
public interface FieldConsumer {

	public void accept(Field t) throws IllegalAccessException;
}
