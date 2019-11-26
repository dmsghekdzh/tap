package com.nemosw.spigot.tap.event.entity.impl;

import com.nemosw.spigot.tap.event.entity.EntityProvider;

/**
 * @author Nemo
 */
final class EventEntityProvider
{
    private final Class<?> eventClass;

    private final EntityProvider provider;

    public EventEntityProvider(Class<?> eventClass, EntityProvider provider)
    {
        this.eventClass = eventClass;
        this.provider = provider;
    }

    public Class<?> getEventClass()
    {
        return eventClass;
    }

    public EntityProvider getProvider()
    {
        return provider;
    }
}
