package com.nemosw.spigot.tap.debug.event.entity;

import com.nemosw.spigot.tap.debug.DebugProcess;
import com.nemosw.spigot.tap.event.entity.EntityEventManager;
import com.nemosw.spigot.tap.plugin.TapPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

/**
 * @author Nemo
 */
public class EntityEventDebug extends DebugProcess
{
    EntityEventManager entityEventManager;

    @Override
    public void onStart()
    {
        entityEventManager = EntityEventManager.create(TapPlugin.getInstance());

        //모든 엔티티에게 우클릭시 1회성 폭발 이벤트 추가
        World world = Bukkit.getWorlds().get(0);

        for (Entity entity : world.getEntities())
        {
            DebugListener listener = new DebugListener();

            listener.setRegisteredEntityListener(entityEventManager.registerEvents(entity, listener));
        }
    }

    @Override
    public void onStop()
    {
        entityEventManager.destroy();
        entityEventManager = null;
    }
}
