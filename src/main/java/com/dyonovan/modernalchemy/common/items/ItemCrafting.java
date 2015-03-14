package com.dyonovan.modernalchemy.common.items;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.lib.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemCrafting extends Item {

    String unLoc;

    public ItemCrafting(String unLoc, int stackSize) {
        this.setUnlocalizedName(Constants.MODID + ":" + unLoc);
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
        this.setMaxStackSize(stackSize);

        this.unLoc = unLoc;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon(Constants.MODID + ":" + unLoc);
    }
}
