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
import net.minecraft.world.World;

public class ItemPattern extends Item {

    protected static IIcon iconBlankPattern, iconRecordedPattern;

    public ItemPattern() {
        this.setUnlocalizedName(Constants.MODID + ":pattern");
        this.setCreativeTab(ItemReplication.tabItemReplication);
        this.setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        iconBlankPattern = register.registerIcon(Constants.MODID + ":blank_pattern");
        iconRecordedPattern = register.registerIcon(Constants.MODID + ":recorded_pattern");
        this.itemIcon = iconBlankPattern;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World par2World, EntityPlayer player)
    {
        if(player.isSneaking())
        {
            if(itemstack.stackTagCompound != null)
            {
                itemstack.stackTagCompound = null;
                this.itemIcon = iconBlankPattern;
            }
        }
        return itemstack;
    }

    public void setIconRecordedPattern() {this.itemIcon = iconRecordedPattern;}


    /*public static String getRecordedPattern(ItemStack pattern){
        if(pattern != null && pattern.stackTagCompound != null)
            return pattern.stackTagCompound.getString("Item");
        return "";
    }*/

}
