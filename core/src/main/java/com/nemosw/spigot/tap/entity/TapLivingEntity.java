package com.nemosw.spigot.tap.entity;

import com.nemosw.spigot.tap.Tap;
import com.nemosw.spigot.tap.item.TapItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;

public interface TapLivingEntity extends TapEntity
{

    static LivingEntity wrapLiving(LivingEntity entity)
    {
        return Tap.ENTITY.wrapEntity(entity);
    }

    LivingEntity getBukkitEntity();

    float getEyeHeight();

    double getHealth();

    TapItemStack getEquipment(EquipmentSlot slot);

    void setEquipment(EquipmentSlot slot, TapItemStack item);

}
