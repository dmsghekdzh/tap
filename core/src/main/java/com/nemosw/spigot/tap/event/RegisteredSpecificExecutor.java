package com.nemosw.spigot.tap.event;

import com.nemosw.mox.collections.Node;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

final class RegisteredSpecificExecutor
{

    SpecificListener listener;

    SpecificEventExecutor executor;

    Node<RegisteredSpecificExecutor> node;

    RegisteredSpecificExecutor(SpecificListener listener, SpecificEventExecutor executor)
    {
        this.listener = listener;
        this.executor = executor;
    }

    void remove()
    {
        this.executor = null;
        this.node.clear();
        this.node = null;
        this.listener = null;
    }

    void execute(Event event)
    {
        if (this.node == null || (this.executor.ignoreCancelled && event instanceof Cancellable && ((Cancellable) event).isCancelled()))
            return;

        try
        {
            this.executor.execute(this.listener, event);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }

}
