package com.nemosw.spigot.tap.event.specific;

import com.nemosw.spigot.tap.event.ASMEventExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import java.util.*;

public final class SpecificEventManager
{

    private static final EventListenerExecutor EXECUTOR = new EventListenerExecutor();


    static final SpecificEventKey EVENT_KEY = new SpecificEventKey();

    private final Map<Class<? extends Event>, EventListener> registeredListeners = new HashMap<>();

    private final Map<Class<? extends SpecificListener>, SpecificEventExecutor[]> registeredExecutors = new HashMap<>();

    private final Map<Entity, SpecificEntity> entities = new WeakHashMap<>();
    private Plugin plugin;

    public void registerEvents(Plugin plugin)
    {
        if (this.plugin != null)
            throw new IllegalArgumentException("Already registered SpecificEventManager");

        this.plugin = plugin;
        ASMEventExecutor.registerEvents(new EntityEventListener(this), plugin);
    }

    private SpecificEventExecutor[] createExecutor(Class<? extends SpecificListener> clazz)
    {
        SpecificEventExecutor[] executors = this.registeredExecutors.get(clazz);

        if (executors == null)
        {
            this.registeredExecutors.put(clazz, executors = ASMSpecificEventExecutor.createExecutors(clazz));

            for (SpecificEventExecutor executor : executors)
            {
                Class<? extends Event> handlerClass = executor.handlerClass;
                EventListener listener = this.registeredListeners.get(handlerClass);

                if (listener == null)
                {
                    listener = new EventListener();
                    this.plugin.getServer().getPluginManager().registerEvent(handlerClass, listener, EventPriority.HIGH, EXECUTOR, this.plugin, false);
                    this.plugin.getLogger().info("Entity listener registered: " + handlerClass.getName());
                    this.registeredListeners.put(handlerClass, listener);
                }

                listener.addExtractor(executor.specificExtractor);
            }
        }

        return executors;
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

        SpecificEventExecutor[] executors = createExecutor(listener.getClass());

        SpecificEntity specificEntity = this.entities.get(entity);

        if (specificEntity == null)
            this.entities.put(entity, specificEntity = new SpecificEntity());

        return specificEntity.registerEvents(listener, executors);
    }

    void handleEvent(Entity entity, SpecificEventKey key, Event event)
    {
        SpecificEntity specificEntity = this.entities.get(entity);

        if (specificEntity != null)
            specificEntity.handleEvent(key, event);
    }

    public void unregisterAll()
    {
        for (EventListener listener : registeredListeners.values())
            HandlerList.unregisterAll(listener);

        registeredListeners.clear();
        registeredExecutors.clear();
        entities.clear();
    }

    void removeEntity(LivingEntity entity)
    {
        SpecificEntity specificEntity = this.entities.remove(entity);

        if (specificEntity != null)
            specificEntity.clear();
    }

    private static class EventListenerExecutor implements EventExecutor
    {
        @Override
        public void execute(Listener listener, Event event)
        {
            ((EventListener) listener).onEvent(event);
        }
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
                        handleEvent(entity, EVENT_KEY.set(eventClass, specificExtractor), event);
                }
            }
        }
    }

}
