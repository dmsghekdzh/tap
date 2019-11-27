package com.nemosw.spigot.tap.event.entity.impl;

import com.nemosw.spigot.tap.event.entity.EntityProvider;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nemo
 */
final class EventTools
{
    private static final Map<Class<?>, Class<?>> REG_CLASSES = new HashMap<>();

    private static final EventEntityProvider[] DEFAULT_PROVIDERS;

    static
    {
        // 기본 개체 제공자 초기화
        Class<?>[] classes = DefaultProvider.class.getDeclaredClasses();
        List<EventEntityProvider> defaultProviders = new ArrayList<>(classes.length);

        for (Class<?> clazz : classes)
        {
            if (EntityProvider.class.isAssignableFrom(clazz))
            {
                try
                {
                    defaultProviders.add(new EventEntityProvider((EntityProvider) clazz.newInstance()));
                }
                catch (Exception e)
                {
                    throw new AssertionError(e);
                }
            }
        }

        DEFAULT_PROVIDERS = defaultProviders.toArray(new EventEntityProvider[0]);
    }

    static Class<?> getRegistrationClass(Class<?> eventClass)
    {
        Class<?> handlerClass = REG_CLASSES.get(eventClass);

        if (handlerClass == null)
        {
            try
            {
                eventClass.getDeclaredMethod("getHandlerList");
            }
            catch (NoSuchMethodException e)
            {
                Class<?> superClass = eventClass.getSuperclass();

                if (superClass != null && !superClass.equals(Event.class) && Event.class.isAssignableFrom(eventClass.getSuperclass()))
                {
                    handlerClass = getRegistrationClass(eventClass.getSuperclass().asSubclass(Event.class));
                    REG_CLASSES.put(eventClass, handlerClass);
                }
                else
                {
                    throw new IllegalArgumentException("Unable to find handler list for event " + eventClass.getName() + ". Static getHandlerList method required!");
                }
            }
        }

        return handlerClass;
    }

    static EventEntityProvider findDefaultProvider(Class<?> eventClass)
    {
        for (EventEntityProvider provider : DEFAULT_PROVIDERS)
        {
            if (provider.getEventClass().isAssignableFrom(eventClass))
                return provider;
        }

        throw new IllegalArgumentException("Not found DefaultProvider for " + eventClass);
    }
}
