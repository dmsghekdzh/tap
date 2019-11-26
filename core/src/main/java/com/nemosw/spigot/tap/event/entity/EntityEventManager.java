package com.nemosw.spigot.tap.event.entity;

import com.nemosw.mox.collections.CleanableWeakHashMap;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Nemo
 */
public class EntityEventManager implements EventExecutor
{

    private final Plugin plugin;

    private final EntityEventPriority priority;

    private final Map<Entity, EventEntity> entities;

    public EntityEventManager(Plugin plugin)
    {
        this(plugin, EntityEventPriority.HIGH);
    }

    public EntityEventManager(Plugin plugin, EntityEventPriority priority)
    {
        this.plugin = plugin;
        this.priority = priority;
        this.entities = new CleanableWeakHashMap<>(EventEntity::clear);
    }

    public Plugin getPlugin()
    {
        return plugin;
    }

    public EntityEventPriority getPriority()
    {
        return priority;
    }

    private class EventListener implements Listener
    {

        private final List<EventEntityProvider> providerList = new LinkedList<>();

        private EventEntityProvider[] bakedProviders;

        @EventHandler
        public void onEvent(Event event)
        {
            for (EventEntityProvider provider : getProviders())
            {
                Entity entity = provider.getEntityFrom(event);
                EventEntity eventEntity = entities.get(entity);

                if (eventEntity != null)
                {
                    EventKey key = new EventKey(event.getClass(), provider);
                    eventEntity.handleEvent(key, event);
                }
            }
        }

        private EventEntityProvider[] getProviders()
        {
            EventEntityProvider[] providers = bakedProviders;

            if (providers != null)
                return providers;

            return bakedProviders = providerList.toArray(new EventEntityProvider[0]);
        }
    }

}
