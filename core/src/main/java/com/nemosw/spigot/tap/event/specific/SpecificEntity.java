package com.nemosw.spigot.tap.event.specific;

import com.nemosw.mox.collections.LinkedNodeList;
import org.bukkit.event.Event;

import java.util.HashMap;

final class SpecificEntity
{

    private final LinkedNodeList<RegisteredSpecificListener> registeredListeners = new LinkedNodeList<>();

    private final HashMap<SpecificEventKey, EventEntityHandler> handlers = new HashMap<>();

    RegisteredSpecificListener registerEvents(SpecificListener listener, SpecificEventExecutor[] executors)
    {
        int length = executors.length;
        RegisteredSpecificExecutor[] registereds = new RegisteredSpecificExecutor[length];
        HashMap<SpecificEventKey, EventEntityHandler> handlers = this.handlers;

        for (int i = 0; i < length; i++)
        {
            SpecificEventExecutor executor = executors[i];
            RegisteredSpecificExecutor registered = new RegisteredSpecificExecutor(listener, executor);
            SpecificEventKey key = executor.eventKey;
            EventEntityHandler handler = handlers.get(key);

            if (handler == null)
                handlers.put(key, handler = new EventEntityHandler());

            handler.registerExecutor(registered);

            registereds[i] = registered;
        }

        RegisteredSpecificListener registeredListener = new RegisteredSpecificListener(registereds);
        registeredListener.node = this.registeredListeners.addNode(registeredListener);

        return registeredListener;
    }

    void handleEvent(SpecificEventKey key, Event event)
    {
        EventEntityHandler handler = this.handlers.get(key);

        if (handler != null)
        {
            for (RegisteredSpecificExecutor executor : handler.getExecutors())
            {
                executor.execute(event);
            }
        }
    }

    void clear()
    {
        LinkedNodeList<RegisteredSpecificListener> registeredListeners = this.registeredListeners;

        while (!registeredListeners.isEmpty())
            registeredListeners.peek().unregister();

        registeredListeners.clear();

        HashMap<SpecificEventKey, EventEntityHandler> handlers = this.handlers;

        for (EventEntityHandler handler : handlers.values())
            handler.clear();

        handlers.clear();
    }
}
