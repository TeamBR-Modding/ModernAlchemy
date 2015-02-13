package com.dyonovan.itemreplication.items;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.blocks.replicator.BlockFrame;
import com.dyonovan.itemreplication.entities.EntityLaserNode;
import com.dyonovan.itemreplication.lib.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLaserNode extends Item {

    public ItemLaserNode() {
        super();
        setCreativeTab(ItemReplication.tabItemReplication);
        setUnlocalizedName(Constants.MODID + ":laserNodeItem");
        this.setTextureName(Constants.MODID + ":cube");
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {
            if(world.getBlock(x, y, z) instanceof BlockFrame) {
                world.spawnEntityInWorld(new EntityLaserNode(world, x + 0.5, y, z + 0.5, side));
                stack.stackSize--;
                return true;
            }
            return false;
        } else {
            return false;
        }
    }
}
