package com.dyonovan.modernalchemy.items.buckets;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.lib.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemSlag extends Item {

    public ItemSlag() {
        this.setUnlocalizedName(Constants.MODID + ":itemSlag");
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
        this.setMaxStackSize(64);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon(Constants.MODID + ":slag");
    }
}
