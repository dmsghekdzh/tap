package com.nemosw.spigot.tap.event.specific;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class SpecificUnallocListener implements Listener
{

    private final SpecificEventManager manager;

    SpecificUnallocListener(SpecificEventManager manager)
    {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event)
    {
        LivingEntity entity = event.getEntity();

        if (entity instanceof Player)
            return;

        this.manager.unregisterEntity(entity);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        this.manager.unregisterEntity(event.getPlayer());
    }

}
