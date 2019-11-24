package com.nemosw.spigot.tap.block;

import com.nemosw.spigot.tap.Tap;

import java.util.List;

public interface TapBlock
{

    TapBlock AIR = Tap.BLOCK.getBlock("air");

    String getTextureId();

    int getId();

    TapBlockData getBlockData();

    TapBlockData getBlockData(int data);

    List<TapBlockData> getBlockDataList();

}
