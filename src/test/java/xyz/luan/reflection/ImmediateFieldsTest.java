package xyz.luan.reflection;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import xyz.luan.reflection.entities.Car;
import xyz.luan.reflection.entities.TopLevel;

public class ImmediateFieldsTest {

    @Test
    public void testSimpleClass() {
        Assert.assertEquals(3, ReflectionUtils.getImmediateFields(Car.class).size());
    }

    @Test
    public void testInheritanceClass() {
        Assert.assertEquals(1, ReflectionUtils.getImmediateFields(TopLevel.StaticChild.class).size());
    }

    @Test
    public void testInnerClass() {
        Assert.assertEquals(1, ReflectionUtils.getImmediateFields(TopLevel.Inner.class).size());
    }

    @Test
    public void testInnerInheritanceClass() {
        Assert.assertEquals(1, ReflectionUtils.getImmediateFields(TopLevel.InnerChild.class).size());
    }

    @Test
    public void testNestedStaticClass() {
        Assert.assertEquals(0, ReflectionUtils.getImmediateFields(TopLevel.Static.class).size());
    }

    @Test
    public void testNestedStaticInheritanceClass() {
        Assert.assertEquals(1, ReflectionUtils.getImmediateFields(TopLevel.StaticChild.class).size());
    }

    @Test
    public void testAnonymous() {
        Assert.assertEquals(0, ReflectionUtils.getImmediateFields(new HashMap<String, String>(){
            private static final long serialVersionUID = 8954422592898495388L;
        }.getClass()).size());

        Assert.assertEquals(2, ReflectionUtils.getImmediateFields(new HashMap<String, String>(){
            private static final long serialVersionUID = 8954422592898495388L;
            private String a, b;
            {
                put(a, b);
            }
        }.getClass()).size());
    }

    @Test
    public void testJavaClasses() {
        Assert.assertEquals(0, ReflectionUtils.getFieldsRecursively(String.class).size());
        Assert.assertEquals(0, ReflectionUtils.getFieldsRecursively(ArrayList.class).size());
        Assert.assertEquals(1, ReflectionUtils.getFieldsRecursively(new ArrayList<Integer>(){
            private static final long serialVersionUID = -938486839854925183L;
            int a;
            {
                add(a);
            }
        }.getClass()).size());
    }
}
