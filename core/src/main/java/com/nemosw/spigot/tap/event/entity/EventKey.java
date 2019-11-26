package com.nemosw.spigot.tap.event.entity;

import org.bukkit.event.Event;

/**
 * @author Nemo
 */
final class EventKey
{
    private final Class<? extends Event> eventClass;

    private final EventEntityProvider provider;

    private int hashCode;

    EventKey(Class<? extends Event> eventClass, EventEntityProvider provider)
    {
        this.eventClass = eventClass;
        this.provider = provider;
    }

    public Class<? extends Event> getEventClass()
    {
        return eventClass;
    }

    public EventEntityProvider getProvider()
    {
        return provider;
    }

    @Override
    public int hashCode()
    {
        int hashCode = this.hashCode;

        if (hashCode == 0)
            return this.hashCode = eventClass.hashCode() ^ provider.hashCode();

        return hashCode;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;

        if (obj instanceof EventKey)
        {
            EventKey other = (EventKey) obj;

            return eventClass == other.eventClass && provider == other.provider;
        }

        return false;
    }
}
