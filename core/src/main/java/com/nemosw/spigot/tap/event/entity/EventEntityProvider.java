package com.nemosw.spigot.tap.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import java.lang.reflect.ParameterizedType;

/**
 * @author Nemo
 */
final class EventEntityProvider
{

    private final Class<?> eventClass;

    private final EntityProvider provider;

    EventEntityProvider(EntityProvider provider)
    {
        this.eventClass = (Class<?>) ((ParameterizedType) provider.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.provider = provider;
    }

    Entity getEntityFrom(Event event)
    {
        if (eventClass.isAssignableFrom(event.getClass()))
        {
            provider.getEntityFrom(event);
        }

        return null;
    }

}
