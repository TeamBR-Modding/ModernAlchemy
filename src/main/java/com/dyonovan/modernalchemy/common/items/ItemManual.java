package com.dyonovan.modernalchemy.common.items;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.client.gui.GuiManual;
import com.dyonovan.modernalchemy.lib.Constants;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemManual extends Item {

    public ItemManual() {
        super();
        this.setMaxStackSize(1);
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon(Constants.MODID + ":manual");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote) FMLCommonHandler.instance().showGuiScreen(new GuiManual());
        return stack;
    }
}
