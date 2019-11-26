package com.nemosw.spigot.tap.event.entity;

import org.bukkit.event.Event;

/**
 * @author Nemo
 */
interface EntityEventExecutor
{
    void execute(EntityListener listener, Event event);
}
