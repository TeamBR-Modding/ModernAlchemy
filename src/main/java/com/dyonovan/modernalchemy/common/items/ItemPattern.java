package com.dyonovan.modernalchemy.common.items;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.helpers.GuiHelper;
import cpw.mods.fml.common.registry.GameRegistry;
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
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
        this.setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        iconBlankPattern = register.registerIcon(Constants.MODID + ":blank_pattern");
        iconRecordedPattern = register.registerIcon(Constants.MODID + ":recorded_pattern");
        //this.itemIcon = iconBlankPattern;
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
                    itemstack = new ItemStack(ItemHandler.itemReplicatorPattern, 1);
                }
            }
        }
        return itemstack;
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4) {
        super.addInformation(itemstack, player, list, par4);

        if (itemstack.hasTagCompound()) {
            String itemName = itemstack.getTagCompound().getString("Item");
            Item item = GameRegistry.findItem(itemName.split(":")[0], itemName.split(":")[1]);
            if(item != null)
                list.add(GuiHelper.GuiColor.YELLOW + "Item Stored: " + GuiHelper.GuiColor.TURQUISE + item.getItemStackDisplayName(new ItemStack(item, 1)));
            list.add(GuiHelper.GuiColor.YELLOW + "Replicator Cost: " + GuiHelper.GuiColor.TURQUISE + itemstack.getTagCompound().getInteger("Value"));
            list.add(GuiHelper.GuiColor.YELLOW + "Quantity Returned: " + GuiHelper.GuiColor.TURQUISE + itemstack.getTagCompound().getInteger("Qty"));
            list.add(GuiHelper.GuiColor.YELLOW + "Quality: " + GuiHelper.GuiColor.TURQUISE + itemstack.getTagCompound().getFloat("Quality") + "%");
        }
    }

}
