package com.dyonovan.modernalchemy.blocks.arcfurnace.dummies;

import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.dummies.TileDummyAirValve;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.dummies.TileDummyEnergyReciever;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDummyEnergyReciever extends BlockDummy {
    public BlockDummyEnergyReciever(String name) {
        super(name);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int par2) {
        return new TileDummyEnergyReciever();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = register.registerIcon(Constants.MODID + ":blastFurnaceEnergy");
        active = register.registerIcon(Constants.MODID + ":blastFurnaceEnergyActive");
    }
}