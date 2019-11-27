package com.nemosw.spigot.tap.event.entity;

/**
 * 등록된 {@link EntityListener}를 관리 할 수 있는 클래스입니다.
 *
 * @author Nemo
 */
public interface RegisteredEntityListener
{

    EntityListener getListener();

    /**
     * 등록된 {@link EntityListener}를 해제합니다.
     */
    void unregister();

}
