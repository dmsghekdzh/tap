package com.nemosw.spigot.tap.event.entity;

import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nemo
 */
class EventEntity
{
    private final Map<Class<?>, EntityHandlerList> handlers = new HashMap<>();

    void handleEvent(Class<?> eventClass, EventEntityProvider provider, Event event)
    {

    }

    public void clear()
    {
    }
}
