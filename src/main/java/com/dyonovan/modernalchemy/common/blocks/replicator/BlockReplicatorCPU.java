package com.dyonovan.modernalchemy.common.blocks.replicator;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.common.blocks.BlockBase;
import com.dyonovan.modernalchemy.common.blocks.BlockModernAlchemy;
import com.dyonovan.modernalchemy.common.blocks.IExpellable;
import com.dyonovan.modernalchemy.handlers.GuiHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.common.tileentity.replicator.TileReplicatorCPU;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.block.BlockRotationMode;
import openmods.infobook.BookDocumentation;

@BookDocumentation
public class BlockReplicatorCPU extends BlockModernAlchemy {

    @SideOnly(Side.CLIENT)
    public static class Icons {
        public static IIcon side;
    }

    public BlockReplicatorCPU() {
        super(Material.iron);
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
        setRotationMode(BlockRotationMode.FOUR_DIRECTIONS);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {
        this.blockIcon = iconregister.registerIcon(Constants.MODID + ":replicatorCPU");
        Icons.side = blockIcon;

        setTexture(ForgeDirection.NORTH, blockIcon);
        setTexture(ForgeDirection.SOUTH, blockIcon);
        setTexture(ForgeDirection.EAST, blockIcon);
        setTexture(ForgeDirection.WEST, blockIcon);
        setTexture(ForgeDirection.UP, blockIcon);
        setTexture(ForgeDirection.DOWN, blockIcon);
    }
}
