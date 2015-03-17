package com.dyonovan.modernalchemy.common.blocks.replicator;

import com.dyonovan.modernalchemy.common.blocks.BlockModernAlchemy;
import com.dyonovan.modernalchemy.common.tileentity.replicator.TileReplicatorStand;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import openmods.infobook.BookDocumentation;

@BookDocumentation
public class BlockReplicatorStand extends BlockModernAlchemy {

    public BlockReplicatorStand() {
        super(Material.iron);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {
        this.blockIcon = iconregister.registerIcon("minecraft:iron_block");
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
}
