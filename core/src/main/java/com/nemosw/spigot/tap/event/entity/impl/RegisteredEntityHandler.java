package com.nemosw.spigot.tap.event.entity.impl;

import com.nemosw.mox.collections.Node;
import com.nemosw.spigot.tap.event.entity.EntityListener;

/**
 * {@link EventEntity}에 {@link com.nemosw.spigot.tap.event.entity.EntityHandler}를 등록하기 위한 클래스입니다.
 *
 * @author Nemo
 */
final class RegisteredEntityHandler
{

    private final EntityListener listener;

    private final HandlerStatement statement;

    Node<RegisteredEntityHandler> node;

    RegisteredEntityHandler(EntityListener listener, HandlerStatement statement)
    {
        this.listener = listener;
        this.statement = statement;
    }

    public EntityListener getListener()
    {
        return listener;
    }

    public HandlerStatement getStatement()
    {
        return statement;
    }

    /**
     * 핸들러의 노드 연결을 해제합니다.
     * {@link org.bukkit.entity.Entity}가 메모리에서 해제되거나 더 이상 유효하지 않을때 호출됩니다.
     */
    void clear()
    {
        node.clear();
        node = null;
    }


    /**
     * 핸들러의 노드 연결을 약하게 해제합니다.
     * 외부에서 {@link com.nemosw.spigot.tap.event.entity.RegisteredEntityListener}를 등록 해제 할 때 호출됩니다.
     */
    void unregister()
    {
        node.unlink();
        node = null;
    }

}
