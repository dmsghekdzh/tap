package com.nemosw.spigot.tap.event.specific;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SpecificEventHandler
{

    Class<?> extractor() default SpecificExtractor.class;

    SpecificEventPriority priority() default SpecificEventPriority.NORMAL;

    boolean ignoreCancelled() default false;

}
