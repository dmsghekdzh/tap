package com.nemosw.spigot.tap.event.entity.impl;

import com.nemosw.mox.collections.CleanableWeakHashMap;
import com.nemosw.spigot.tap.event.entity.EntityEventManager;
import com.nemosw.spigot.tap.event.entity.EntityListener;
import com.nemosw.spigot.tap.event.entity.RegisteredEntityListener;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nemo
 */
final class EntityEventManagerImpl implements EntityEventManager
{

    private final Plugin plugin;

    private final EventPriority priority;

    private final Map<Class<?>, ListenerStatement> statements = new HashMap<>();

    private final CleanableWeakHashMap<Entity, EventEntity> entities = new CleanableWeakHashMap<>(EventEntity::unregisterAll);

    public EntityEventManagerImpl(Plugin plugin, EventPriority priority)
    {
        this.plugin = plugin;
        this.priority = priority;
    }

    @Override
    public RegisteredEntityListener registerEvents(Entity entity, EntityListener listener)
    {

    }

    @Override
    public void unregisterAll()
    {
        CleanableWeakHashMap<Entity, EventEntity> entities = this.entities;

        for (EventEntity eventEntity : entities.values())
        {
            eventEntity.unregisterAll();
        }

        entities.clear();
    }

    private class EventListener implements Listener
    {
        private final ArrayList<EventEntityProvider> providerList = new ArrayList<>();

        private EventEntityProvider[] providers;

        @SuppressWarnings("unchecked")
        public void onEvent(Event event)
        {
            for (EventEntityProvider provider : getProviders())
            {
                Class<?> eventClass = event.getClass();

                if (provider.getEventClass().isAssignableFrom(eventClass))
                {
                    Entity entity = provider.getProvider().getFrom(event);

                    if (entity != null)
                    {
                        EventEntity eventEntity = entities.get(entity);

                        if (eventEntity != null)
                        {
                            Class<?> regClass = EventTools.getRegistrationClass(eventClass);
                            EntityHandlerList handlers = eventEntity.getHandlers(regClass);

                            handlers.callEvent(event, provider, eventClass, entity);

                        }
                    }
                }
            }
        }

        private EventEntityProvider[] getProviders()
        {
            EventEntityProvider[] providers = this.providers;

            if (providers != null)
                return providers;

            return this.providers = providerList.toArray(new EventEntityProvider[0]);
        }

        private void addProvider(EventEntityProvider provider)
        {
            this.providerList.add(provider);
            this.providers = null;
        }
    }

}
