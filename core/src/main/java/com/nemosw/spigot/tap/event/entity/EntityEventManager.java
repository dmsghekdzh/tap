package com.nemosw.spigot.tap.event.entity;

import com.nemosw.spigot.tap.event.entity.impl.EntityEventManagerImpl;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

/**
 * 지정된 {@link Entity}에게 {@link EntityListener}를 등록하기 위한 관리 클래스입니다.
 *
 * @author Nemo
 */
public interface EntityEventManager
{

    static EntityEventManager create(Plugin plugin)
    {
        return create(plugin, EventPriority.HIGH);
    }

    static EntityEventManager create(Plugin plugin, EventPriority priority)
    {
        return new EntityEventManagerImpl(plugin, priority);
    }

    /**
     * 지정한 {@link Entity}에게 {@link EntityListener}를 등록합니다.
     * <br>{@link Entity}객체는 {@link java.lang.ref.WeakReference}로 관리되어 메모리에서 해제될 시 등록된 {@link EntityListener}도 해제됩니다.
     */
    RegisteredEntityListener registerEvents(Entity entity, EntityListener listener);


    /**
     * 지정한 {@link Entity}에게 등록된 모든 {@link EntityListener}를 해제합니다.
     */
    void unregister(Entity entity);

    /**
     * 등록된 모든 {@link EntityListener}를 해제합니다.
     */
    void unregisterAll();


    /**
     * 등록된 모든 리스너를 해제하고 {@link EntityEventManager}를 비활성화합니다.
     */
    void destroy();

    /**
     * 객체가 유효한지 확인합니다.
     */
    boolean isValid();
}
