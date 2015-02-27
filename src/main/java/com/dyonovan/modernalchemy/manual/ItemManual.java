package com.dyonovan.modernalchemy.manual;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.handlers.GuiHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.manual.page.ManualPages;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemManual extends Item {

    public ItemManual() {
        super();
        this.setUnlocalizedName(Constants.MODID + ":itemManual");
        this.setMaxStackSize(1);
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon(Constants.MODID + ":manual");
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean isCurrentItem)
    {
        if(!world.isRemote && !itemstack.hasTagCompound())
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("Page0", "Main Page");
            itemstack.setTagCompound(tag);
        }
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if(world.isRemote) {
            if(ManualPages.instance.getPage(world.getBlock(x, y, z).getUnlocalizedName()) != null) {
                if(stack.hasTagCompound() && ItemManual.getCurrentPage(stack).equalsIgnoreCase(world.getBlock(x, y, z).getUnlocalizedName()))
                    player.openGui(ModernAlchemy.instance, GuiHandler.MANUAL_GUI_ID, world, (int) player.posX, (int) player.posY, (int) player.posZ);
                else
                    ManualPages.instance.openPage(ManualPages.instance.getPage(world.getBlock(x, y, z).getUnlocalizedName()));
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
    {
        if(world.isRemote) {
            if (itemStack.hasTagCompound())
                entityPlayer.openGui(ModernAlchemy.instance, GuiHandler.MANUAL_GUI_ID, world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
        }
        return itemStack;
    }

    public static String getLastPage(ItemStack itemStack) {
        int i = 0;
        if(itemStack.hasTagCompound()) {
            while(itemStack.getTagCompound().hasKey("Page" + i)) {
                i++;
            }
            return "Page" + (i - 1);
        }
        return "Main Page";
    }

    public static String getCurrentPage(ItemStack itemStack) {
        int i = 0;
        if(itemStack.hasTagCompound()) {
            while(itemStack.getTagCompound().hasKey("Page" + i)) {
                i++;
            }
            return itemStack.getTagCompound().getString("Page" + 1);
        }
        return "Main Page";
    }

    public static void addPageToVisitedPages(ItemStack itemstack, String pageId) {
        int i = 0;
        if(itemstack.hasTagCompound()) {
            while(itemstack.getTagCompound().hasKey("Page" + i)) {
                i++;
            }
            itemstack.getTagCompound().setString("Page" + i, pageId);
        }
        else {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("Page0", "Main Page");
            itemstack.setTagCompound(tag);
        }
    }

    public static void deleteLastPage(ItemStack itemstack) {
        int i = 0;
        if (itemstack.hasTagCompound()) {
            NBTTagCompound tag = new NBTTagCompound();
            while(itemstack.getTagCompound().hasKey("Page" + i)) {
                if(itemstack.getTagCompound().hasKey("Page" + (i + 2)))
                    break;
                tag.setString("Page" + i, itemstack.getTagCompound().getString("Page" + i));
                i++;
            }
            itemstack.setTagCompound(tag);
        }
        else {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("Page0", "Main Page");
            itemstack.setTagCompound(tag);
        }
    }
}
