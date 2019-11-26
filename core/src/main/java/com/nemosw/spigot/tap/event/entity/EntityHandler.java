package com.nemosw.spigot.tap.event.entity;

/**
 * @author Nemo
 */
public @interface EntityHandler
{

    EntityEventPriority priority() default EntityEventPriority.NORMAL;

    Class<? extends EventEntityProvider> provider() default EventEntityProvider.class;

    boolean ignoreCancelled() default false;

}
