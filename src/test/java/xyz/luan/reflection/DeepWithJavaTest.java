package xyz.luan.reflection;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import xyz.luan.reflection.entities.Car;
import xyz.luan.reflection.entities.TopLevel;

public class DeepWithJavaTest {

    @Test
    public void testSimpleClass() {
        Assert.assertEquals(3, ReflectionUtils.getFieldsRecursivelyIncludingJavaClasses(Car.class).size());
    }

    @Test
    public void testInheritanceClass() {
        Assert.assertEquals(1, ReflectionUtils.getFieldsRecursivelyIncludingJavaClasses(TopLevel.StaticChild.class).size());
    }

    @Test
    public void testInnerClass() {
        Assert.assertEquals(1, ReflectionUtils.getFieldsRecursivelyIncludingJavaClasses(TopLevel.Inner.class).size());
    }

    @Test
    public void testInnerInheritanceClass() {
        Assert.assertEquals(2, ReflectionUtils.getFieldsRecursivelyIncludingJavaClasses(TopLevel.InnerChild.class).size());
    }

    @Test
    public void testNestedStaticClass() {
        Assert.assertEquals(0, ReflectionUtils.getFieldsRecursivelyIncludingJavaClasses(TopLevel.Static.class).size());
    }

    @Test
    public void testNestedStaticInheritanceClass() {
        Assert.assertEquals(1, ReflectionUtils.getFieldsRecursivelyIncludingJavaClasses(TopLevel.StaticChild.class).size());
    }

    @Test
    public void testAnonymous() {
        Assert.assertEquals(8, ReflectionUtils.getFieldsRecursivelyIncludingJavaClasses(new HashMap<String, String>(){
            private static final long serialVersionUID = 8954422592898495388L;
        }.getClass()).size());

        Assert.assertEquals(10, ReflectionUtils.getFieldsRecursivelyIncludingJavaClasses(new HashMap<String, String>(){
            private static final long serialVersionUID = 8954422592898495388L;
            private String a, b;
            {
                put(a, b);
            }
        }.getClass()).size());
    }

    @Test
    public void testJavaClasses() {
        Assert.assertEquals(2, ReflectionUtils.getFieldsRecursivelyIncludingJavaClasses(String.class).size());
        Assert.assertEquals(3, ReflectionUtils.getFieldsRecursivelyIncludingJavaClasses(ArrayList.class).size());
        Assert.assertEquals(4, ReflectionUtils.getFieldsRecursivelyIncludingJavaClasses(new ArrayList<Integer>() {
            private static final long serialVersionUID = -938486839854925183L;
            int a;
            {
                add(a);
            }
        }.getClass()).size());
    }
}
