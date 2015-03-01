package com.dyonovan.modernalchemy.items;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemOreActinium extends Item {

    public ItemOreActinium() {
        this.setUnlocalizedName(Constants.MODID + ":itemActiniumDust");
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon(Constants.MODID + ":item_actinium");
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int i, boolean whoknows) {
        super.onUpdate(itemStack, world, entity, i, whoknows);
        //TODO cancel if wearing rad proof helm
        if (entity instanceof EntityPlayer && !ConfigHandler.poisonDust) {
            ((EntityPlayer) entity).addPotionEffect(new PotionEffect(Potion.poison.getId(), 100, 10));
        }
    }
}
