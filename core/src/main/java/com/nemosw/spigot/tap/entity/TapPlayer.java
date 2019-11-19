package com.nemosw.spigot.tap.entity;

import com.nemosw.spigot.tap.Tap;
import com.nemosw.spigot.tap.inventory.TapPlayerInventory;
import com.nemosw.spigot.tap.item.TapItemStack;
import org.bukkit.entity.Player;

public interface TapPlayer extends TapLivingEntity
{

    static TapPlayer wrapPlayer(Player player)
    {
        return Tap.ENTITY.wrapEntity(player);
    }

    Player getBukkitEntity();

    int getLevel();

    int getFoodLevel();

    TapPlayerInventory getInventory();

    TapItemStack getHeldItemMainHand();

    TapItemStack getHeldItemOffHand();

}
