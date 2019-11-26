package com.nemosw.spigot.tap.event.entity.impl;

import com.nemosw.mox.collections.Node;
import com.nemosw.spigot.tap.event.entity.EntityListener;
import com.nemosw.spigot.tap.event.entity.RegisteredEntityListener;

/**
 * @author Nemo
 */
final class RegisteredEntityListenerImpl implements RegisteredEntityListener
{

    private final EntityListener listener;

    private final RegisteredEntityHandler[] handlers;

    Node<RegisteredEntityListenerImpl> node;

    RegisteredEntityListenerImpl(EntityListener listener, RegisteredEntityHandler[] handlers)
    {
        this.listener = listener;
        this.handlers = handlers;
    }

    @Override
    public EntityListener getListener()
    {
        return listener;
    }

    public RegisteredEntityHandler[] getHandlers()
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
