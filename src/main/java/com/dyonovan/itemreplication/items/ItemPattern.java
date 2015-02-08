package com.dyonovan.itemreplication.items;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.lib.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * Created by Tim on 2/5/2015.
 */
public class ItemPattern extends Item {

    public ItemPattern() {
        this.setUnlocalizedName(Constants.MODID + ":pattern");
        this.setCreativeTab(ItemReplication.tabItemReplication);
    }


    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        return super.getIcon(stack, renderPass, player, usingItem, useRemaining);
    }

//    @SideOnly(Side.CLIENT)
//    @Override
//    protected String getIconString() {
//        return Constants.MODID + ":recorded_pattern";
//    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon(Constants.MODID + ":blank_pattern");
        this.itemIcon = register.registerIcon(Constants.MODID + ":recorded_pattern");
    }
}
