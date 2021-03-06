# Reflection Utils
[![Build Status](https://github.com/luanpotter/reflection-utils/workflows/Test/badge.svg?branch=master&event=push)](https://github.com/luanpotter/reflection-utils/actions)
[![Maven Central](https://img.shields.io/maven-central/v/xyz.luan/reflection-utils)](https://search.maven.org/artifact/xyz.luan/reflection-utils)

Simple library that changes the base Reflection API on Java, making it easier to work with.
It uses cowtowncoder/java-classmate for type parsing.

## Features

### ReflectionUtils

Utility class with common functions not found on Class<?> API
 * isInnerClass(Class<?> clazz) : returns true if this class is an inner class, that is, if it is a non-static nested class (either annonymous, method local or class local)
 * getImmediateFields(Class<?> clazz) : returns all the fields (all visibilities) immediate from this class, excluding static and synthetic fields
 * getFieldsRecursively(Class<?> clazz) : like getImmediateFields, but goes up class hierarchy adding parent classes fields as well, except for Java classes fields 
 * getFieldsRecursivelyIncludingJavaClasses(Class<?> clazz) : like getFieldsRecursively, but add Java classes fields, like the array inside String class

### TypedClass
 
You can now convert your classes to this convenient wrapper. Call TypedClass.create using a Field object.  
The returned TypedClass is like a class, but with tons of neat features, allowing for easy access to the new TYPE_USE Java 8's annotation target annotations.
