package com.nemosw.spigot.tap.event.specific;

import com.google.common.collect.ImmutableList;
import com.nemosw.spigot.tap.event.ASMEventExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import java.util.*;

public final class SpecificEventManager
{

    private static final EventExecutor EXECUTOR = (listener, event) -> ((EventListener) listener).onEvent(event); // Bukkit event bridge
    private final Plugin plugin;
    private final EventPriority priority;
    private final SpecificUnallocListener unallocListener;
    private final Map<Class<? extends SpecificListener>, List<SpecificEventExecutor>> registeredExecutors = new HashMap<>();

    private final Map<Class<? extends Event>, EventListener> registeredListeners = new HashMap<>();

    private SpecificEventManager(Plugin plugin, EventPriority priority)
    {
        this.plugin = plugin;
        this.priority = priority;
        this.unallocListener = new SpecificUnallocListener(this);

        ASMEventExecutor.registerEvents(this.unallocListener, plugin);
    }

    private final Map<Entity, SpecificEntity> entities = new WeakHashMap<>();

    public static SpecificEventManager create(Plugin plugin, EventPriority priority)
    {
        return new SpecificEventManager(plugin, priority);
    }

    public void registerListener(Class<? extends SpecificListener> clazz)
    {
        createExecutor(clazz);
    }

    public RegisteredSpecificListener registerEvents(Entity entity, SpecificListener listener)
    {
        if (entity == null)
            throw new NullPointerException("Entity cannot be null");
        if (listener == null)
            throw new NullPointerException("Listener cannot be null");

        List<SpecificEventExecutor> executors = createExecutor(listener.getClass());

        SpecificEntity specificEntity = this.entities.get(entity);

        if (specificEntity == null)
            this.entities.put(entity, specificEntity = new SpecificEntity());

        return specificEntity.registerEvents(listener, executors);
    }

    private List<SpecificEventExecutor> createExecutor(Class<? extends SpecificListener> clazz)
    {
        return registeredExecutors.computeIfAbsent(clazz, listenerClass -> {
            List<SpecificEventExecutor> executors = ImmutableList.copyOf(ASMSpecificEventExecutor.createExecutors(clazz));

            for (SpecificEventExecutor executor : executors)
            {
                Class<? extends Event> handlerClass = executor.handlerClass;
                EventListener listener = this.registeredListeners.get(handlerClass);

                if (listener == null)
                {
                    listener = new EventListener();
                    plugin.getServer().getPluginManager().registerEvent(handlerClass, listener, priority, EXECUTOR, plugin, false);
                    plugin.getLogger().info("Entity listener registered: " + handlerClass.getName());
                    this.registeredListeners.put(handlerClass, listener);
                }

                listener.addExtractor(executor.specificExtractor);
            }

            return executors;
        });
    }

    public void unregisterEntity(Entity entity)
    {
        SpecificEntity specificEntity = entities.remove(entity);

        if (specificEntity != null)
            specificEntity.clear();
    }

    public void unregisterListener(Entity entity, SpecificListener listener)
    {
        SpecificEntity specificEntity = entities.get(entity);

        if (specificEntity != null)
        {
            new NonBlock
            specificEntity.
        }
    }

    public void unregisterAll()
    {
        HandlerList.unregisterAll(unallocListener);
        for (EventListener listener : registeredListeners.values())
            HandlerList.unregisterAll(listener);

        registeredListeners.clear();
        registeredExecutors.clear();
        entities.clear();
    }

    private void handleEvent(Entity entity, SpecificEventKey key, Event event)
    {
        SpecificEntity specificEntity = entities.get(entity);

        if (specificEntity != null)
            specificEntity.handleEvent(key, event);
    }

    private class EventListener implements Listener
    {

        private final Set<SpecificExtractor<? extends Event>> specificExtractors = new LinkedHashSet<>();

        private SpecificExtractor<? extends Event>[] baked;

        public void addExtractor(SpecificExtractor<? extends Event> extractor)
        {
            if (this.specificExtractors.add(extractor))
                this.baked = null;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        public void onEvent(Event event)
        {
            if (this.baked == null)
                this.baked = this.specificExtractors.toArray(new SpecificExtractor[0]);

            Class<? extends Event> eventClass = event.getClass();

            for (SpecificExtractor specificExtractor : this.baked)
            {
                if (specificExtractor.eventClass.isAssignableFrom(eventClass))
                {
                    Entity entity = specificExtractor.getEntity(event);

                    if (entity != null)
                        handleEvent(entity, new SpecificEventKey(eventClass, specificExtractor), event);
                }
            }
        }
    }

}
