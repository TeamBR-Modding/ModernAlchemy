package com.dyonovan.modernalchemy.blocks;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.blocks.teslacoil.BlockTeslaBase;
import com.dyonovan.modernalchemy.blocks.teslacoil.BlockTeslaCoil;
import com.dyonovan.modernalchemy.blocks.teslacoil.BlockTeslaStand;
import com.dyonovan.modernalchemy.util.Location;
import com.dyonovan.modernalchemy.util.WorldUtils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import openmods.block.OpenBlock;

public abstract class BlockModernAlchemy extends OpenBlock {

    public BlockModernAlchemy(Material mat) {
        super(mat);
        this.setHardness(2.0F);
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
    }

    @Override
    protected Object getModInstance() {
        return ModernAlchemy.instance;
    }

    /* Will implement when fully converted
    public boolean useWrench(World world, int x, int y, int z) {
        if (!world.isRemote) {
            if (this instanceof BlockTeslaBase || this instanceof BlockTeslaStand) {
                Location loc = new Location(x, y + 1, z);
                int count = 0;
                while (true) {
                    if (world.getBlock(loc.x, loc.y, loc.z) instanceof BlockTeslaStand || world.getBlock(loc.x, loc.y, loc.z) instanceof BlockTeslaCoil) {
                        loc.y += 1;
                        count += 1;
                    } else {
                        doBreak(world, loc.x, loc.y - 1, loc.z, count);
                        break;
                    }
                }
            }
            WorldUtils.expelItem(world, x, y, z, new ItemStack(Item.getItemFromBlock(this)));
            world.setBlockToAir(x, y, z);
        }
        return true;
    }

    private void doBreak(World world, int x, int y, int z, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                WorldUtils.expelItem(world, x, y - i, z, new ItemStack(Item.getItemFromBlock(world.getBlock(x, y - i, z))));
                world.setBlockToAir(x, y - i, z);
            }
        }
    }
    */
}