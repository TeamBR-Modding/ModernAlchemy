package com.dyonovan.modernalchemy.items;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.blocks.replicator.BlockReplicatorFrame;
import com.dyonovan.modernalchemy.entities.EntityLaserNode;
import com.dyonovan.modernalchemy.lib.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLaserNode extends Item {

    public ItemLaserNode() {
        super();
        setCreativeTab(ModernAlchemy.tabModernAlchemy);
        setUnlocalizedName(Constants.MODID + ":laserNodeItem");
        this.setTextureName(Constants.MODID + ":laserNodeItem");
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {
            if(world.getBlock(x, y, z) instanceof BlockReplicatorFrame && (side != 0 && side != 1)) {
                world.spawnEntityInWorld(new EntityLaserNode(world, x + 0.5, y, z + 0.5, side));
                if(!player.capabilities.isCreativeMode)
                    stack.stackSize--;
                return true;
            }
            return false;
        } else {
            return false;
        }
    }
}
