package com.nemosw.spigot.tap.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

/**
 * @author Nemo
 */
public interface EntityProvider<T extends Event>
{

    Entity getFrom(T event);

}
