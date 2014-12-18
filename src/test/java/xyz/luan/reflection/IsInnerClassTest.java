package xyz.luan.reflection;

import java.lang.annotation.ElementType;
import java.util.Collection;
import java.util.function.Predicate;

import javax.swing.JButton;

import org.junit.Assert;
import org.junit.Test;

import xyz.luan.reflection.entities.Car;
import xyz.luan.reflection.entities.CarType;
import xyz.luan.reflection.entities.InterfaceClass;
import xyz.luan.reflection.entities.TopLevel;

public class IsInnerClassTest {

    @Test
    public void enums() {
        Assert.assertFalse(ReflectionUtils.isInnerClass(CarType.class));
        Assert.assertFalse(ReflectionUtils.isInnerClass(ElementType.class));
        Assert.assertFalse(ReflectionUtils.isInnerClass(TopLevel.EnumClass.class));
    }

    @Test
    public void primitives() {
        Assert.assertFalse(ReflectionUtils.isInnerClass(int.class));
        Assert.assertFalse(ReflectionUtils.isInnerClass(char[].class));
        Assert.assertFalse(ReflectionUtils.isInnerClass(JButton.class));
    }

    @Test
    public void interfaces() {
        Assert.assertFalse(ReflectionUtils.isInnerClass(InterfaceClass.class));
        Assert.assertFalse(ReflectionUtils.isInnerClass(Collection.class));
    }

    @Test
    public void topLevelJavaClasses() {
        Assert.assertFalse(ReflectionUtils.isInnerClass(String.class));
        Assert.assertFalse(ReflectionUtils.isInnerClass(Integer.class));
        Assert.assertFalse(ReflectionUtils.isInnerClass(JButton.class));
        Assert.assertFalse(ReflectionUtils.isInnerClass(Predicate.class));
    }

    @Test
    public void topLevelCustomClasses() {
        Assert.assertFalse(ReflectionUtils.isInnerClass(Car.class));
        Assert.assertFalse(ReflectionUtils.isInnerClass(IsInnerClassTest.class));
        Assert.assertFalse(ReflectionUtils.isInnerClass(TopLevel.class));
    }

    @Test
    public void nestedButStaticClasses() {
        Assert.assertFalse(ReflectionUtils.isInnerClass(TopLevel.Static.class));
        Assert.assertFalse(ReflectionUtils.isInnerClass(InterfaceClass.Inner.class));
    }

    @Test
    public void actuallyInnerClasses() {
        Assert.assertTrue(ReflectionUtils.isInnerClass(TopLevel.Inner.class));
        Assert.assertTrue(ReflectionUtils.isInnerClass(TopLevel.Static.Inner.class));
    }

    @Test
    public void testAnonymous() {
        Assert.assertTrue(ReflectionUtils.isInnerClass(new IsInnerClassTest(){ }.getClass()));
    }
}
