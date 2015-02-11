package com.dyonovan.itemreplication.items;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.handlers.ItemHandler;
import com.dyonovan.itemreplication.lib.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class ItemPattern extends Item {

    @SideOnly(Side.CLIENT)
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
    public IIcon getIconIndex(ItemStack itemstack) {
        if(itemstack.hasTagCompound()) return iconRecordedPattern;
        else return iconBlankPattern;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World par2World, EntityPlayer player)
    {
        if(player.isSneaking())
        {
            if(itemstack.hasTagCompound()) {

                if (itemstack.getTagCompound().hasKey("Item")) {
                    itemstack = new ItemStack(ItemHandler.itemPattern, 1);
                }
            }
        }
        return itemstack;
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4) {
        super.addInformation(itemstack, player, list, par4);

        if (itemstack.hasTagCompound()) {
            list.add("This has an item");
        }
    }

}
