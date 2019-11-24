package com.nemosw.spigot.tap.event.specific;

import com.nemosw.mox.collections.LinkedNodeList;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class SpecificEntity
{

    private final List<RegisteredSpecificListener> registeredListeners = new ArrayList<>();

    private final Map<SpecificEventKey, EventSpecificHandler> handlers = new HashMap<>();

    RegisteredSpecificListener registerEvents(SpecificListener listener, List<SpecificEventExecutor> executors)
    {
        int size = executors.size();
        RegisteredSpecificExecutor[] registereds = new RegisteredSpecificExecutor[size];
        Map<SpecificEventKey, EventSpecificHandler> handlers = this.handlers;

        for (int i = 0; i < size; i++)
        {
            SpecificEventExecutor executor = executors[i];
            RegisteredSpecificExecutor registered = new RegisteredSpecificExecutor(listener, executor);
            SpecificEventKey key = executor.eventKey;
            EventSpecificHandler handler = handlers.get(key);

            if (handler == null)
                handlers.put(key, handler = new EventSpecificHandler());

            handler.registerExecutor(registered);

            registereds[i] = registered;
        }

        RegisteredSpecificListener registeredListener = new RegisteredSpecificListener(registereds);
        registeredListener.node = this.registeredListeners.addNode(registeredListener);

        return registeredListener;
    }

    void handleEvent(SpecificEventKey key, Event event)
    {
        EventSpecificHandler handler = this.handlers.get(key);

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

        HashMap<SpecificEventKey, EventSpecificHandler> handlers = this.handlers;

        for (EventSpecificHandler handler : handlers.values())
            handler.clear();

        handlers.clear();
    }
}
