package com.dyonovan.modernalchemy.common.blocks;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.common.blocks.teslacoil.BlockTeslaBase;
import com.dyonovan.modernalchemy.common.blocks.teslacoil.BlockTeslaCoil;
import com.dyonovan.modernalchemy.common.blocks.teslacoil.BlockTeslaStand;
import com.dyonovan.modernalchemy.util.Location;
import com.dyonovan.modernalchemy.util.WorldUtils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class BlockBase extends BlockContainer {

    public BlockBase(Material mat) {
        super(mat);
        this.setHardness(2.0F);
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
    }
}
