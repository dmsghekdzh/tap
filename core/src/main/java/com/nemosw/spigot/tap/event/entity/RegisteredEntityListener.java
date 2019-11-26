package com.nemosw.spigot.tap.event.entity;

/**
 * @author Nemo
 */
public interface RegisteredEntityListener
{

    EntityListener getListener();

    void unregister();

}
