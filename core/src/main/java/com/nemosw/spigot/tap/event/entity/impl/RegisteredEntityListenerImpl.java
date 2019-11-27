package com.nemosw.spigot.tap.event.entity.impl;

import com.nemosw.mox.collections.Node;
import com.nemosw.spigot.tap.event.entity.EntityListener;
import com.nemosw.spigot.tap.event.entity.RegisteredEntityListener;

/**
 * {@link RegisteredEntityListener}의 구현체입니다.
 *
 * @author Nemo
 */
final class RegisteredEntityListenerImpl implements RegisteredEntityListener
{

    private final EntityListener listener;

    private final RegisteredEntityHandler[] handlers;

    Node<RegisteredEntityListenerImpl> node;

    RegisteredEntityListenerImpl(ListenerStatement statement, EntityListener listener)
    {
        this.listener = listener;

        HandlerStatement[] handlerStatements = statement.getHandlerStatements();
        RegisteredEntityHandler[] entityHandlers = new RegisteredEntityHandler[handlerStatements.length];

        for (int i = 0, length = handlerStatements.length; i < length; i++)
        {
            entityHandlers[i] = new RegisteredEntityHandler(listener, handlerStatements[i]);
        }

        this.handlers = entityHandlers;
    }

    @Override
    public EntityListener getListener()
    {
        return listener;
    }

    RegisteredEntityHandler[] getHandlers()
    {
        return handlers;
    }

    @Override
    public void unregister()
    {
        node.clear();
        node = null;

        for (RegisteredEntityHandler handler : handlers)
        {
            handler.unregister();
        }
    }

}
