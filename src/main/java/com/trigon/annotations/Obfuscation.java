package com.trigon.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;

@Inherited
@Target(value = {ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface Obfuscation {

  boolean exclude() default true;

  boolean applyToMembers() default true;
}