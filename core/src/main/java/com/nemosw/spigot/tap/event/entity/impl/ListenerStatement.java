package com.nemosw.spigot.tap.event.entity.impl;

/**
 * {@link com.nemosw.spigot.tap.event.entity.EntityListener}의 정보를 관리하는 클래스입니다.
 *
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

    public HandlerStatement[] getHandlerStatements()
    {
        return statements;
    }

}
