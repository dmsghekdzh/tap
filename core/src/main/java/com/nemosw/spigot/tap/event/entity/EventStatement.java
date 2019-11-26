package com.nemosw.spigot.tap.event.entity;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

/**
 * @author Nemo
 */
final class EventStatement
{

    private final Class<? extends EntityListener> listenerClass;

    private final Class<?> eventClass;

    private final EventEntityProvider provider;

    private final EntityEventPriority priority;

    private final boolean ignoreCancelled;

    private final EventKey eventKey;

    private final EntityEventExecutor executor;

    public EventStatement(Class<? extends EntityListener> listenerClass, Class<?> eventClass, EventEntityProvider provider, EntityEventPriority priority, boolean ignoreCancelled, EventKey eventKey, EntityEventExecutor executor)
    {
        this.listenerClass = listenerClass;
        this.eventClass = eventClass;
        this.provider = provider;
        this.priority = priority;
        this.ignoreCancelled = ignoreCancelled;
        this.eventKey = eventKey;
        this.executor = executor;
    }

    public Class<? extends EntityListener> getListenerClass()
    {
        return listenerClass;
    }

    public Class<?> getEventClass()
    {
        return eventClass;
    }

    public EventEntityProvider getProvider()
    {
        return provider;
    }

    public EntityEventPriority getPriority()
    {
        return priority;
    }

    public boolean isIgnoreCancelled()
    {
        return ignoreCancelled;
    }

    public EventKey getEventKey()
    {
        return eventKey;
    }

    void callEvent(EntityListener listener, Event event)
    {
        if ((ignoreCancelled && ((Cancellable) event).isCancelled()) || !eventClass.isAssignableFrom(event.getClass()))
            return;

        executor.execute(listener, event);
    }

}
