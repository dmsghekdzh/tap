package com.nemosw.spigot.tap.event.entity.impl;

import com.nemosw.spigot.tap.event.entity.EntityProvider;

/**
 * {@link EntityProvider}의 컨테이너 클래스입니다.
 *
 * @author Nemo
 */
final class EventEntityProvider
{

    private final Class<?> eventClass;

    private final EntityProvider provider;

    EventEntityProvider(EntityProvider provider)
    {
        eventClass = EventTools.getGenericEventType(provider.getClass());
        this.provider = provider;
    }

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
