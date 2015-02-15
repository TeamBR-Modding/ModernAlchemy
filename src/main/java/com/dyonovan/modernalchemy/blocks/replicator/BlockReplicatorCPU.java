package com.dyonovan.modernalchemy.blocks.replicator;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.blocks.BlockBase;
import com.dyonovan.modernalchemy.handlers.GuiHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.tileentity.replicator.TileReplicatorCPU;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockReplicatorCPU extends BlockBase {

    public BlockReplicatorCPU() {
        super(Material.iron);
        this.setBlockName(Constants.MODID + ":blockReplicatorCPU");
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {
        this.blockIcon = iconregister.registerIcon(Constants.MODID + ":frame_energy");
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileReplicatorCPU();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);

        if (world.isRemote)
        {
            return true;
        }
        else
        {
            TileReplicatorCPU tile = (TileReplicatorCPU)world.getTileEntity(x, y, z);
            if(tile != null) {
                player.openGui(ModernAlchemy.instance, GuiHandler.REPLICATOR_CPU_GUI_ID, world, x, y, z);
            }
            return true;
        }
    }
}
