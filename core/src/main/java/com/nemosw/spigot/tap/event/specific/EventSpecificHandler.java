package com.nemosw.spigot.tap.event.specific;

import com.nemosw.mox.collections.EventNodeList;
import com.nemosw.mox.collections.Node;

import java.util.ArrayList;
import java.util.EnumMap;

final class EventSpecificHandler implements EventNodeList.NodeListener<RegisteredSpecificExecutor>
{

    private final EnumMap<SpecificEventPriority, EventNodeList<RegisteredSpecificExecutor>> executorsByPriority = new EnumMap<>(SpecificEventPriority.class);

    private RegisteredSpecificExecutor[] executors;

    void registerExecutor(RegisteredSpecificExecutor registered)
    {
        EnumMap<SpecificEventPriority, EventNodeList<RegisteredSpecificExecutor>> executorsByPriority = this.executorsByPriority;
        SpecificEventExecutor executor = registered.executor;
        SpecificEventPriority priority = executor.priority;
        EventNodeList<RegisteredSpecificExecutor> registeredExecutors = executorsByPriority.get(priority);

        if (registeredExecutors == null)
        {
            executorsByPriority.put(priority, registeredExecutors = new EventNodeList<>());
            registeredExecutors.registerListener(EventNodeList.EventType.UNLINK, this);
        }

        registered.node = registeredExecutors.addNode(registered);
        this.executors = null;
    }

    @Override
    public void onEvent(EventNodeList.EventType type, Node<RegisteredSpecificExecutor> node, RegisteredSpecificExecutor item)
    {
        this.executors = null;
    }

    public RegisteredSpecificExecutor[] getExecutors()
    {
        RegisteredSpecificExecutor[] executors = this.executors;

        if (executors == null)
        {
            ArrayList<RegisteredSpecificExecutor> list = new ArrayList<>();

            for (EventNodeList<RegisteredSpecificExecutor> executorList : this.executorsByPriority.values())
                list.addAll(executorList);

            this.executors = executors = list.toArray(new RegisteredSpecificExecutor[0]);
        }

        return executors;
    }

    void clear()
    {
        this.executorsByPriority.clear();
        this.executors = null;
    }

}
