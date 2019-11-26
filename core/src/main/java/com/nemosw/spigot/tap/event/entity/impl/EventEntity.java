package com.nemosw.spigot.tap.event.entity.impl;

import com.nemosw.mox.collections.EventNodeList;

import java.util.HashMap;

/**
 * @author Nemo
 */
final class EventEntity
{

    private final EventNodeList<RegisteredEntityListenerImpl> listeners = new EventNodeList<>();

    private final HashMap<Class<?>, EntityHandlerList> slots = new HashMap<>();

    void register(RegisteredEntityListenerImpl listener)
    {
        listener.node = listeners.addNode(listener);

        for (RegisteredEntityHandler handler : listener.getHandlers())
        {
            HandlerStatement statement = handler.getStatement();
            EntityHandlerList handlers = slots.compute(statement.getRegistrationClass(), (handlerClass, handlerList) -> new EntityHandlerList());
            handlers.register(handler);
        }
    }

    void unregisterAll()
    {
        listeners.clear();

        for (EntityHandlerList handlerList : slots.values())
        {
            handlerList.clear();
        }

        slots.clear();
    }

    EntityHandlerList getHandlers(Class<?> handlerClass)
    {
        return slots.get(handlerClass);
    }

}
