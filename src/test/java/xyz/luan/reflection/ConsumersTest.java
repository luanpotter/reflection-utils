package xyz.luan.reflection;

import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.junit.Test;

public class ConsumersTest {

	private class Holder {
		private String s1, s2, s3;
	}

	@Test
	public void consumeFieldsRecursivelyTest() {
		final String testString = "Howdy!";
		final Holder h = new Holder();
		final AtomicInteger counter = new AtomicInteger(0);
		ReflectionUtils.consumeFieldsRecursively(Holder.class, (f) -> {
			f.set(h, testString);
			Assert.assertEquals(testString, f.get(h));
			counter.incrementAndGet();
		});
		Assert.assertEquals(3, counter.intValue());
	}
}
