Reflection Utils
===

[![Build Status](https://api.shippable.com/projects/53e77cda9d47409e0059b9ff/badge/master)](https://www.shippable.com/projects/53e77cda9d47409e0059b9ff)

Very simple one file library that allows you to get all the fields of a given class in a very simplistic ways, making sure to workaround all the caveats Java creates.

Examples
---

 * isInnerClass(Class<?> clazz) : returns true if this class is an inner class, that is, if it is a non-static nested class (either annonymous, method local or class local)
 * getImmediateFields(Class<?> clazz) : returns all the fields (all visibilities) immediate from this class, excluding static and synthetic fields
 * getFieldsRecursively(Class<?> clazz) : like getImmediateFields, but goes up class hierarchy adding parent classes fields as well, except for Java classes fields 
 * getFieldsRecursivelyIncludingJavaClasses(Class<?> clazz) : like getFieldsRecursively, but add Java classes fields, like the array inside String class
