package xyz.luan.reflection;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import xyz.luan.reflection.entities.Car;
import xyz.luan.reflection.entities.TopLevel;

public class DeepFieldsTest {

    @Test
    public void testSimpleClass() {
        Assert.assertEquals(3, ReflectionUtils.getFieldsRecursivelyExceptJavaClasses(Car.class).size());
    }

    @Test
    public void testInheritanceClass() {
        Assert.assertEquals(1, ReflectionUtils.getFieldsRecursivelyExceptJavaClasses(TopLevel.StaticChild.class).size());
    }

    @Test
    public void testInnerClass() {
        Assert.assertEquals(1, ReflectionUtils.getFieldsRecursivelyExceptJavaClasses(TopLevel.Inner.class).size());
    }

    @Test
    public void testInnerInheritanceClass() {
        Assert.assertEquals(2, ReflectionUtils.getFieldsRecursivelyExceptJavaClasses(TopLevel.InnerChild.class).size());
    }

    @Test
    public void testNestedStaticClass() {
        Assert.assertEquals(0, ReflectionUtils.getFieldsRecursivelyExceptJavaClasses(TopLevel.Static.class).size());
    }

    @Test
    public void testNestedStaticInheritanceClass() {
        Assert.assertEquals(1, ReflectionUtils.getFieldsRecursivelyExceptJavaClasses(TopLevel.StaticChild.class).size());
    }

    @Test
    public void testAnonymous() {
        Assert.assertEquals(0, ReflectionUtils.getFieldsRecursivelyExceptJavaClasses(new HashMap<String, String>(){
            private static final long serialVersionUID = 8954422592898495388L;
        }.getClass()).size());

        Assert.assertEquals(2, ReflectionUtils.getFieldsRecursivelyExceptJavaClasses(new HashMap<String, String>(){
            private static final long serialVersionUID = 8954422592898495388L;
            private String a, b;
            {
                put(a, b);
            }
        }.getClass()).size());
    }
}
