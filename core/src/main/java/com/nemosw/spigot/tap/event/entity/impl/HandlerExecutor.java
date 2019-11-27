package com.nemosw.spigot.tap.event.entity.impl;

import com.nemosw.spigot.tap.event.entity.EntityListener;
import org.bukkit.event.Event;

/**
 * {@link EntityListener}의 {@link com.nemosw.spigot.tap.event.entity.EntityHandler} 메소드를 실행하는 클래스입니다.
 *
 * @author Nemo
 */
public interface HandlerExecutor
{

    void execute(EntityListener listener, Event event);

}
