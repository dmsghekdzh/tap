package com.nemosw.spigot.tap.event.entity;

import org.bukkit.event.Event;

/**
 * @author Nemo
 */
final class RegisteredEntityListener
{

    private final EventStatement statement;

    private final EntityListener listener;

    RegisteredEntityListener(EventStatement statement, EntityListener listener)
    {
        this.statement = statement;
        this.listener = listener;
    }

    public EventStatement getStatement()
    {
        return statement;
    }

    public EntityListener getListener()
    {
        return listener;
    }

    final void callEvent(Event event)
    {
        statement.callEvent(listener, event);
    }

}
