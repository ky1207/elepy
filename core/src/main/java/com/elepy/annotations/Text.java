package com.elepy.annotations;

import com.elepy.models.TextType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Text {
    TextType value() default TextType.TEXTFIELD;

    int minimumLength() default 0;

    int maximumLength() default Integer.MAX_VALUE;
}
