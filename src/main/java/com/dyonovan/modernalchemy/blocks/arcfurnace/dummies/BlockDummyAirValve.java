package com.dyonovan.modernalchemy.blocks.arcfurnace.dummies;

import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.manual.component.ComponentBase;
import com.dyonovan.modernalchemy.manual.component.ComponentItemRender;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.dummies.TileDummyAirValve;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockDummyAirValve extends BlockDummy {
    @SideOnly(Side.CLIENT)
    public static class Icons {
        public static IIcon inActive;
        public static IIcon active;
    }

    public BlockDummyAirValve() {
        super();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        setDefaultTexture(blockIcon = register.registerIcon(Constants.MODID + ":blastFurnaceAirValve"));
        Icons.active = register.registerIcon(Constants.MODID + ":blastFurnaceAirValveActive");
        Icons.inActive = blockIcon;
    }
}
