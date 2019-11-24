package com.nemosw.spigot.tap.event.specific;

import org.bukkit.event.Event;

public abstract class SpecificEventExecutor
{
    final Class<? extends Event> eventClass;
    final Class<? extends Event> handlerClass;
    final SpecificExtractor<? extends Event> specificExtractor;
    final SpecificEventKey eventKey;
    final SpecificEventPriority priority;
    final boolean ignoreCancelled;

    public SpecificEventExecutor(Class<? extends Event> eventClass, Class<? extends Event> handlerClass, SpecificExtractor<? extends Event> specificExtractor,
                                 SpecificEventPriority priority, boolean ignoreCancelled
    )
    {
        this.eventClass = eventClass;
        this.handlerClass = handlerClass;
        this.specificExtractor = specificExtractor;
        this.eventKey = specificExtractor.createEventKey(eventClass);
        this.priority = priority;
        this.ignoreCancelled = ignoreCancelled;
    }

    public Class<?> getEventClass()
    {
        return this.eventClass;
    }

    public Class<?> getHandlerClass()
    {
        return this.handlerClass;
    }

    public abstract void execute(SpecificListener listener, Event event);
}
