package com.dyonovan.itemreplication.blocks;

import com.dyonovan.itemreplication.util.WorldUtils;
import com.dyonovan.itemreplication.helpers.WrenchHelper;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class BlockBase extends BlockContainer{

    public BlockBase(Material mat) {
        super(mat);
    }

    public boolean useWrench(World world, int x, int y, int z) {
        if(!world.isRemote) {
            WorldUtils.expelItem(world, x, y, z, new ItemStack(Item.getItemFromBlock(this)));
            world.setBlockToAir(x, y, z);
        }
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (player.isSneaking() && WrenchHelper.isWrench(player.getCurrentEquippedItem().getItem())) {
            useWrench(world, x, y, z);
            return true;
        }
        return false;
    }
}
