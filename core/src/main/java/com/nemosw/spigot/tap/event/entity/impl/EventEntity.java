package com.nemosw.spigot.tap.event.entity.impl;

import com.nemosw.mox.collections.EventNodeList;

import java.util.HashMap;

/**
 * {@link com.nemosw.spigot.tap.event.entity.EntityListener}가 등록되는 클래스입니다.
 *
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

    /**
     * 등록된 모든 리스너를 해제합니다.
     */
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
