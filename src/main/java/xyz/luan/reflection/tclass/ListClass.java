package xyz.luan.reflection.tclass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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

	public void forEach(Object object, BiConsumer<Integer, Object> consumer) {
		if (ref.isArray()) {
			arrayForEach(object, consumer);
		} else {
			listForEach(object, consumer);
		}
	}

	private void listForEach(Object object, BiConsumer<Integer, Object> consumer) {
		final AtomicInteger index = new AtomicInteger(-1);
		Collection.class.cast(object).forEach((el) -> consumer.accept(index.incrementAndGet(), el));
	}

	private void arrayForEach(Object object, BiConsumer<Integer, Object> consumer) {
		for (int i = 0; i < Array.getLength(object); i++) {
			consumer.accept(i, Array.get(object, i));
		}
	}

	public void forEach(Object object, Consumer<Object> consumer) {
		if (ref.isArray()) {
			arrayForEach(object, consumer);
		} else {
			listForEach(object, consumer);
		}
	}

	private void listForEach(Object object, Consumer<Object> consumer) {
		Collection.class.cast(object).forEach(consumer);
	}

	private void arrayForEach(Object object, Consumer<Object> consumer) {
		for (int i = 0; i < Array.getLength(object); i++) {
			consumer.accept(Array.get(object, i));
		}
	}
}
