package com.nemosw.spigot.tap.event.specific;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public abstract class SpecificExtractor<T extends Event>
{
    private final Map<Class<?>, SpecificEventKey> eventKeyByEventClass = new HashMap<>();

    final Class<? extends Event> eventClass;

    @SuppressWarnings("unchecked")
    public SpecificExtractor()
    {
        eventClass = (Class<? extends Event>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    SpecificEventKey createEventKey(Class<?> eventClass)
    {
        SpecificEventKey eventKey = this.eventKeyByEventClass.get(eventClass);

        if (eventKey == null)
            this.eventKeyByEventClass.put(eventClass, eventKey = new SpecificEventKey().set(eventClass, this));

        return eventKey;
    }

    public abstract Entity getEntity(T event);

    static final class BlockBreakEventExtractor extends SpecificExtractor<BlockBreakEvent>
    {
        public Entity getEntity(BlockBreakEvent event)
        {
            return event.getPlayer();
        }
    }

    static final class BlockDamageEventExtractor extends SpecificExtractor<BlockDamageEvent>
    {
        @Override
        public Entity getEntity(BlockDamageEvent event)
        {
            return event.getPlayer();
        }
    }

    static final class BlockIgniteEventExtractor extends SpecificExtractor<BlockIgniteEvent>
    {
        public Entity getEntity(BlockIgniteEvent event)
        {
            return event.getIgnitingEntity();
        }
    }

    static final class BlockPlaceEventExtractor extends SpecificExtractor<BlockPlaceEvent>
    {
        @Override
        public Entity getEntity(BlockPlaceEvent event)
        {
            return event.getPlayer();
        }
    }

    static final class SignChangeEventExtractor extends SpecificExtractor<SignChangeEvent>
    {
        @Override
        public Entity getEntity(SignChangeEvent event)
        {
            return event.getPlayer();
        }
    }

    static final class EnchantItemEventExtractor extends SpecificExtractor<EnchantItemEvent>
    {
        @Override
        public Entity getEntity(EnchantItemEvent event)
        {
            return event.getEnchanter();
        }
    }

    static final class PrepareItemEnchantEventExtractor extends SpecificExtractor<PrepareItemEnchantEvent>
    {
        @Override
        public Entity getEntity(PrepareItemEnchantEvent event)
        {
            return event.getEnchanter();
        }
    }

    static final class SpecificEventExtractor extends SpecificExtractor<EntityEvent>
    {
        public Entity getEntity(EntityEvent event)
        {
            return event.getEntity();
        }
    }

    static final class HangingBreakBySpecificEventExtractor extends SpecificExtractor<HangingBreakByEntityEvent>
    {
        public Entity getEntity(HangingBreakByEntityEvent event)
        {
            return event.getRemover();
        }
    }

    static final class HangingPlaceEventExtractor extends SpecificExtractor<HangingPlaceEvent>
    {
        @Override
        public Entity getEntity(HangingPlaceEvent event)
        {
            return event.getPlayer();
        }
    }

    static final class FurnaceExtractEventExtractor extends SpecificExtractor<FurnaceExtractEvent>
    {
        @Override
        public Entity getEntity(FurnaceExtractEvent event)
        {
            return event.getPlayer();
        }
    }

    static final class InventoryCloseEventExtractor extends SpecificExtractor<InventoryCloseEvent>
    {
        @Override
        public Entity getEntity(InventoryCloseEvent event)
        {
            return event.getPlayer();
        }
    }

    static final class InventoryInteractEventExtractor extends SpecificExtractor<InventoryInteractEvent>
    {
        @Override
        public Entity getEntity(InventoryInteractEvent event)
        {
            return event.getWhoClicked();
        }
    }

    static final class InventoryOpenEventExtractor extends SpecificExtractor<InventoryOpenEvent>
    {
        @Override
        public Entity getEntity(InventoryOpenEvent event)
        {
            return event.getPlayer();
        }
    }

    static final class PlayerEventExtractor extends SpecificExtractor<PlayerEvent>
    {
        @Override
        public Entity getEntity(PlayerEvent event)
        {
            return event.getPlayer();
        }
    }

    static final class VehicleEventExtractor extends SpecificExtractor<VehicleEvent>
    {
        @Override
        public Entity getEntity(VehicleEvent event)
        {
            return event.getVehicle();
        }
    }

    public static final class Damager extends SpecificExtractor<EntityDamageByEntityEvent>
    {
        @Override
        public Entity getEntity(EntityDamageByEntityEvent event)
        {
            return event.getDamager();
        }
    }

    public static final class EntityDamageByEntity
    {
        public static final class Shooter extends SpecificExtractor<EntityDamageByEntityEvent>
        {
            @Override
            public Entity getEntity(EntityDamageByEntityEvent event)
            {
                Entity damager = event.getDamager();

                if (damager instanceof Projectile)
                {
                    ProjectileSource source = ((Projectile) damager).getShooter();

                    if (source instanceof Entity)
                        return (Entity) source;
                }

                return null;
            }
        }
    }

    public static final class ProjectileHit
    {
        public static final class Shooter extends SpecificExtractor<ProjectileHitEvent>
        {
            @Override
            public Entity getEntity(ProjectileHitEvent event)
            {
                ProjectileSource shooter = event.getEntity().getShooter();

                return shooter instanceof Entity ? (Entity) shooter : null;
            }
        }
    }
}