package com.nemosw.spigot.tap.event.entity.impl;

import com.google.common.reflect.TypeToken;
import com.nemosw.spigot.tap.event.entity.EntityHandler;
import com.nemosw.spigot.tap.event.entity.EntityProvider;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.EventExecutor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Nemo
 */
final class EventTools
{
    private static final Map<Class<?>, Class<?>> REG_CLASSES = new HashMap<>();

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

    static ListenerStatement createListenerStatement(Class<?> listenerClass)
    {
        int mod = listenerClass.getModifiers();

        if (!Modifier.isPublic(mod))
            throw new IllegalArgumentException("EntityListener modifier must be public");

        Method[] methods = listenerClass.getMethods();

        class HandlerInfo
        {
            final Method method;
            final Class<?> eventClass;
            final Class<?> providerClass;
            final Class<?> registrationClass;
            final EventPriority priority;
            final boolean ignoreCancelled;

            HandlerInfo(Method method, Class<?> eventClass, Class<?> registrationClass, Class<?> providerClass, EventPriority priority, boolean ignoreCancelled)
            {
                this.method = method;
                this.eventClass = eventClass;
                this.registrationClass = registrationClass;
                this.providerClass = providerClass;
                this.priority = priority;
                this.ignoreCancelled = ignoreCancelled;
            }
        }

        ArrayList<HandlerInfo> infoList = new ArrayList<>(methods.length);
        Set<? extends Class<?>> supers = TypeToken.of(listenerClass).getTypes().rawTypes();

        for (Method method : methods)
        {
            for (Class<?> superClass : supers)
            {
                try
                {
                    Method real = superClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
                    EntityHandler entityHandler = real.getAnnotation(EntityHandler.class);

                    if (entityHandler != null)
                    {
                        Class<?>[] parameterTypes = real.getParameterTypes();

                        if (parameterTypes.length != 1)
                            throw new IllegalArgumentException("@EntityEventHandler methods must require a single argument: " + method);

                        if (method.getReturnType() != void.class)
                            throw new IllegalArgumentException("@EntityEventHandler methods must return 'void': " + method);

                        Class<?> eventClass = parameterTypes[0];

                        if (!Event.class.isAssignableFrom(eventClass))
                            throw new IllegalArgumentException("'" + eventClass.getName() + "' is not event class : " + method);

                        Class<?> registrationClass = getRegistrationClass(eventClass);
                        Class<?> providerClass = entityHandler.provider();

                        infoList.add(new HandlerInfo(method, eventClass, registrationClass, providerClass, entityHandler.priority(), entityHandler.ignoreCancelled()));
                    }
                }
                catch (NoSuchMethodException | SecurityException ignored)
                {
                }
            }
        }

        int size = infoList.size();
        HandlerStatement[] statements = new HandlerStatement[size];

        for (int i = 0; i < size; i++)
        {
            HandlerInfo info = infoList.get(i);

            EventExecutor executor = ASMHandlerExecutor.create(info.method);
            EntityProvider provider =

                    statements[i] = new HandlerStatement(info.eventClass, info.registrationClass, info.providerClass, info.priority, info.ignoreCancelled, executor);
        }

        return new ListenerStatement(listenerClass, statements);
    }

}
