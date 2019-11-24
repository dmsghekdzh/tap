package com.nemosw.spigot.tap.v1_12_R1.block;

import com.google.common.collect.ImmutableList;
import com.nemosw.spigot.tap.block.TapBlock;
import com.nemosw.spigot.tap.block.TapBlockData;
import net.minecraft.server.v1_12_R1.Block;
import net.minecraft.server.v1_12_R1.IBlockData;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public final class NMSBlock implements TapBlock
{

    private final Block block;

    private List<TapBlockData> blockDataList;

    NMSBlock(Block block)
    {
        this.block = block;
    }

    public Block getHandle()
    {
        return block;
    }

    @Override
    public String getTextureId()
    {
        return Block.REGISTRY.b(block).toString();
    }

    @Override
    public int getId()
    {
        return Block.getId(block);
    }

    @Override
    public TapBlockData getBlockData()
    {
        return NMSBlockSupport.getInstance().wrapBlockData(block.getBlockData());
    }

    @Override
    public NMSBlockData getBlockData(int data)
    {
        return NMSBlockSupport.getInstance().wrapBlockData(block.fromLegacyData(data));
    }

    @Override
    public List<TapBlockData> getBlockDataList()
    {
        List<TapBlockData> list = blockDataList;

        if (list != null)
            return list;

        NMSBlockSupport blockSupport = NMSBlockSupport.getInstance();
        List<IBlockData> nmsList = block.s().a();
        List<NMSBlockData> tapList = new ArrayList<>(nmsList.size());

        for (IBlockData data : nmsList)
            tapList.add(blockSupport.wrapBlockData(data));

        return blockDataList = ImmutableList.copyOf(tapList);
    }
}
