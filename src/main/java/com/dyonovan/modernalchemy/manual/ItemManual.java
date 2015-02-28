package com.dyonovan.modernalchemy.manual;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.handlers.GuiHandler;
import com.dyonovan.modernalchemy.lib.Constants;
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
            if(ManualRegistry.instance.getPage(world.getBlock(x, y, z).getUnlocalizedName()) != null) {
                if(stack.hasTagCompound())
                    player.openGui(ModernAlchemy.instance, GuiHandler.MANUAL_GUI_ID, world, (int) player.posX, (int) player.posY, (int) player.posZ);
                else
                    ManualRegistry.instance.openManual();
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
    {
        if(world.isRemote) {
            ManualRegistry.instance.openManual();
            //entityPlayer.openGui(ModernAlchemy.instance, GuiHandler.MANUAL_GUI_ID, world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
        }
        return itemStack;
    }
}
