package com.nemosw.spigot.tap.event.entity;

import org.bukkit.event.Event;

/**
 * @author Nemo
 */
public interface EventInvoker
{

    void invoke(EntityListener listener, Event event);

}
