package com.dyonovan.modernalchemy.common.blocks.teslacoil;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.common.blocks.BlockBase;
import com.dyonovan.modernalchemy.common.blocks.BlockModernAlchemy;
import com.dyonovan.modernalchemy.handlers.GuiHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileTeslaCoil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class BlockTeslaCoil extends BlockModernAlchemy {

    public BlockTeslaCoil() {
        super(Material.iron);
        this.setBlockBounds(0.34375F, 0F, 0.34375F, 0.65625F, 0.9F, 0.65625F);
        setRenderMode(RenderMode.TESR_ONLY);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    //We want the block to have particle effects, so we need to register a block icon even if we don't render it (try falling onto the block and you'll see)
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {
        this.blockIcon = iconregister.registerIcon("minecraft:iron_block");
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity) {
        this.setBlockBounds(0.1F, 0.8F, 0.1F, 0.9F, 0.8F, 0.9F);
        super.addCollisionBoxesToList(world, x, y, z, axisAlignedBB, list, entity);
        this.setBlockBounds(0.34375F, 0F, 0.34375F, 0.65625F, 0.9F, 0.65625F);
        super.addCollisionBoxesToList(world, x, y, z, axisAlignedBB, list, entity);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.getBlock(x, y - 1, z) instanceof BlockTeslaStand;
    }
}
