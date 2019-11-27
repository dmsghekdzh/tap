package com.nemosw.spigot.tap.event.entity;

import com.nemosw.spigot.tap.event.entity.impl.DefaultProvider;
import org.bukkit.event.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Nemo
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EntityHandler
{

    EventPriority priority() default EventPriority.NORMAL;

    Class<? extends EntityProvider> provider() default DefaultProvider.class;

    boolean ignoreCancelled() default false;

}
