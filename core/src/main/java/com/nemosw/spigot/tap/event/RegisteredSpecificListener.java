package com.nemosw.spigot.tap.event;

import com.nemosw.mox.collections.Node;

public final class RegisteredSpecificListener
{

    RegisteredSpecificExecutor[] executors;

    Node<RegisteredSpecificListener> node;

    RegisteredSpecificListener(RegisteredSpecificExecutor[] executors)
    {
        this.executors = executors;
    }

    public final void unregister()
    {
        if (this.node != null)
        {
            this.node.unlink();
            this.node = null;

            RegisteredSpecificExecutor[] executors = this.executors;
            this.executors = null;

            for (int i = 0, length = executors.length; i < length; i++)
            {
                RegisteredSpecificExecutor executor = executors[i];
                executors[i] = null;
                executor.remove();
            }
        }
    }

}
