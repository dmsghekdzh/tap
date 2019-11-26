package com.nemosw.spigot.tap.event.entity.impl;

import com.nemosw.spigot.tap.event.entity.EntityListener;
import org.bukkit.event.Event;

/**
 * @author Nemo
 */
interface HandlerExecutor
{

    void execute(EntityListener listener, Event event);

}
