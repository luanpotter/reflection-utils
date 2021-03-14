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
        Assert.assertEquals(3, ReflectionUtils.getFieldsRecursively(Car.class).size());
    }

    @Test
    public void testInheritanceClass() {
        Assert.assertEquals(1, ReflectionUtils.getFieldsRecursively(TopLevel.StaticChild.class).size());
    }

    @Test
    public void testInnerClass() {
        Assert.assertEquals(1, ReflectionUtils.getFieldsRecursively(TopLevel.Inner.class).size());
    }

    @Test
    public void testInnerInheritanceClass() {
        Assert.assertEquals(2, ReflectionUtils.getFieldsRecursively(TopLevel.InnerChild.class).size());
    }

    @Test
    public void testNestedStaticClass() {
        Assert.assertEquals(0, ReflectionUtils.getFieldsRecursively(TopLevel.Static.class).size());
    }

    @Test
    public void testNestedStaticInheritanceClass() {
        Assert.assertEquals(1, ReflectionUtils.getFieldsRecursively(TopLevel.StaticChild.class).size());
    }

    @Test
    public void testAnonymous() {
        Assert.assertEquals(8, ReflectionUtils.getFieldsRecursively(new HashMap<String, String>(){
            private static final long serialVersionUID = 8954422592898495388L;
        }.getClass()).size());

        Assert.assertEquals(10, ReflectionUtils.getFieldsRecursively(new HashMap<String, String>(){
            private static final long serialVersionUID = 8954422592898495388L;
            private String a, b;
            {
                put(a, b);
            }
        }.getClass()).size());
    }

    @Test
    public void testJavaClasses() {
        Assert.assertEquals(4, ReflectionUtils.getFieldsRecursively(String.class).size());
        Assert.assertEquals(3, ReflectionUtils.getFieldsRecursively(ArrayList.class).size());
        Assert.assertEquals(4, ReflectionUtils.getFieldsRecursively(new ArrayList<Integer>() {
            private static final long serialVersionUID = -938486839854925183L;
            int a;
            {
                add(a);
            }
        }.getClass()).size());
    }
}
