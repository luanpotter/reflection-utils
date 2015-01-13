package xyz.luan.reflection.tclass;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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

	public void forEachKey(Object object, Consumer<Object> consumer) {
		Map<?, ?> map = Map.class.cast(object);
		for (Object key : map.keySet()) {
			consumer.accept(key);
		}
	}

	public void forEachValue(Object object, Consumer<Object> consumer) {
		Map<?, ?> map = Map.class.cast(object);
		for (Object key : map.keySet()) {
			consumer.accept(map.get(key));
		}
	}

	public void forEach(Object object, BiConsumer<Object, Object> consumer) {
		Map<?, ?> map = Map.class.cast(object);
		for (Object key : map.keySet()) {
			consumer.accept(key, map.get(key));
		}
	}
}
