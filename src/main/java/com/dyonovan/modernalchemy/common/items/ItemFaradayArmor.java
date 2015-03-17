package com.dyonovan.modernalchemy.common.items;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.lib.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import openmods.infobook.BookDocumentation;

@BookDocumentation
public class ItemFaradayArmor extends ItemArmor {

    public String texture;

    public ItemFaradayArmor(String unLoc, ArmorMaterial material, String texture, int type) {
        super(material, 0, type);
        this.texture = texture;
        this.setUnlocalizedName(Constants.MODID + ":" + unLoc);
        this.setTextureName(Constants.MODID + ":" + unLoc);
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return Constants.MODID + ":armor/" + this.texture + "_" + (this.armorType == 2 ? "2" : "1") + ".png";
    }
}
