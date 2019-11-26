package com.nemosw.spigot.tap.event.entity.impl;

/**
 * @author Nemo
 */
final class ListenerStatement
{
    private final Class<?> listenerClass;

    private final HandlerStatement[] statements;

    public ListenerStatement(Class<?> listenerClass, HandlerStatement[] statements)
    {
        this.listenerClass = listenerClass;
        this.statements = statements;
    }

    public Class<?> getListenerClass()
    {
        return listenerClass;
    }

    public HandlerStatement[] getStatements()
    {
        return statements;
    }
}
