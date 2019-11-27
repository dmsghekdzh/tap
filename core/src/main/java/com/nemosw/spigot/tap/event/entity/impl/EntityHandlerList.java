package com.nemosw.spigot.tap.event.entity.impl;

import com.nemosw.mox.collections.LinkedNodeList;
import com.nemosw.mox.collections.Node;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

/**
 *
 *
 * @author Nemo
 */
final class EntityHandlerList
{
    private final LinkedNodeList<RegisteredEntityHandler> handlers = new LinkedNodeList<>();

    void register(RegisteredEntityHandler handler)
    {
        LinkedNodeList<RegisteredEntityHandler> handlers = this.handlers;

        if (handlers.size() > 0)
        {
            EventPriority priority = handler.getStatement().getPriority();

            Node<RegisteredEntityHandler> node = handlers.getFirstNode();

            do
            {
                if (priority.compareTo(node.getItem().getStatement().getPriority()) < 0)
                {
                    handler.node = node.linkBefore(handler);
                    return;
                }
            }
            while ((node = node.next()) != null);
        }

        handler.node = handlers.addNode(handler);
    }

    void callEvent(Event event, EventEntityProvider provider, Class<?> eventClass, Entity entity)
    {
        if (handlers.isEmpty())
            return;

        Node<RegisteredEntityHandler> node = handlers.getFirstNode();

        do
        {
            RegisteredEntityHandler handler = node.getItem();
            HandlerStatement statement = handler.getStatement();

            if (statement.getProvider() == provider && statement.getEventClass().isAssignableFrom(eventClass))
            {
                statement.getExecutor().execute(handler.getListener(), event);
            }
        }
        while ((node = node.next()) != null);
    }

    void clear()
    {
        this.handlers.clear();
    }
}
