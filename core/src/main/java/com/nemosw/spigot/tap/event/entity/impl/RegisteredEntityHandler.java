package com.nemosw.spigot.tap.event.entity.impl;

import com.nemosw.mox.collections.Node;
import com.nemosw.spigot.tap.event.entity.EntityListener;

/**
 * @author Nemo
 */
final class RegisteredEntityHandler
{

    private final EntityListener listener;

    private final HandlerStatement statement;

    Node<RegisteredEntityHandler> node;

    RegisteredEntityHandler(EntityListener listener, HandlerStatement statement)
    {
        this.listener = listener;
        this.statement = statement;
    }

    public EntityListener getListener()
    {
        return listener;
    }

    public HandlerStatement getStatement()
    {
        return statement;
    }

    void unregister()
    {
        node.clear();
        node = null;
    }

}
