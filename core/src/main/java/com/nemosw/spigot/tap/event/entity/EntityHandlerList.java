package com.nemosw.spigot.tap.event.entity;

import java.util.ArrayList;
import java.util.EnumMap;

/**
 * @author Nemo
 */
final class EntityHandlerList
{

    private final EnumMap<EntityEventPriority, ArrayList<RegisteredEntityListener>> slots = new EnumMap<>(EntityEventPriority.class);

    private RegisteredEntityListener[] handlers;

    void registerListener(RegisteredEntityListener listener)
    {
        EventStatement statement = listener.getStatement();
        ArrayList<RegisteredEntityListener> slot = slots.computeIfAbsent(statement.getPriority(), priority -> new ArrayList<>());
        slot.add(listener);

        handlers = null;
    }

    private void bake()
    {
        if (handlers == null)
        {
            ArrayList<RegisteredEntityListener> entries = new ArrayList<>();

            for (ArrayList<RegisteredEntityListener> slot : slots.values())
            {
                entries.addAll(slot);
            }

            handlers = entries.toArray(new RegisteredEntityListener[0]);
        }
    }

    RegisteredEntityListener[] getRegisteredListeners()
    {
        RegisteredEntityListener[] handlers = this.handlers;

        if (handlers == null)
        {
            ArrayList<RegisteredEntityListener> entries = new ArrayList<>();

            for (ArrayList<RegisteredEntityListener> registered : slots.values())
            {
                entries.addAll(registered);
            }

            return this.handlers = entries.toArray(new RegisteredEntityListener[0]);
        }

        return handlers;
    }

}
