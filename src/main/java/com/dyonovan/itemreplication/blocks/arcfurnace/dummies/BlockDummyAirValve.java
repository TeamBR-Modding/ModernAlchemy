package com.dyonovan.itemreplication.blocks.arcfurnace.dummies;

import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.tileentity.dummies.TileDummyAirValve;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDummyAirValve extends BlockDummy {
    public BlockDummyAirValve(String name) {
        super(name);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int par2) {
        return new TileDummyAirValve();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = register.registerIcon(Constants.MODID + ":blastFurnaceAirValve");
        active = register.registerIcon(Constants.MODID + ":blastFurnaceAirValveActive");
    }
}
