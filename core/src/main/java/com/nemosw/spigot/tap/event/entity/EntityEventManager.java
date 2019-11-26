package com.nemosw.spigot.tap.event.entity;

import com.nemosw.spigot.tap.event.entity.impl.EntityEventManagerImpl;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

/**
 * @author Nemo
 */
public interface EntityEventManager
{

    static EntityEventManager create(Plugin plugin)
    {
        return create(plugin, EventPriority.HIGH);
    }

    static EntityEventManager create(Plugin plugin, EventPriority priority)
    {
        return new EntityEventManagerImpl(plugin, priority);
    }

    RegisteredEntityListener registerEvents(Entity entity, EntityListener listener);

    void unregisterAll();

}
