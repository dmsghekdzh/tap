package com.nemosw.spigot.tap.debug.event.entity;

import com.nemosw.spigot.tap.event.entity.EntityHandler;
import com.nemosw.spigot.tap.event.entity.EntityListener;
import com.nemosw.spigot.tap.event.entity.EntityProvider;
import com.nemosw.spigot.tap.event.entity.RegisteredEntityListener;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * @author Nemo
 */
public class DebugListener implements EntityListener
{
    private RegisteredEntityListener registeredEntityListener;

    public void setRegisteredEntityListener(RegisteredEntityListener registeredEntityListener)
    {
        this.registeredEntityListener = registeredEntityListener;
    }

    @EntityHandler(provider = EntityProvider.PlayerInteractEntity.Clicked.class)
    public void onClicked(PlayerInteractEntityEvent event)
    {
        Entity entity = event.getRightClicked();

        registeredEntityListener.unregister();

        Location loc = entity.getLocation();
        loc.getWorld().createExplosion(loc, 2.0F);
    }
}
