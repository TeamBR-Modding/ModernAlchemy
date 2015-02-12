package com.dyonovan.itemreplication.blocks.replicator;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.blocks.BlockBase;
import com.dyonovan.itemreplication.handlers.GuiHandler;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.tileentity.TileFrameEnergy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFrameEnergy extends BlockBase {

    public BlockFrameEnergy() {
        super(Material.iron);
        this.setBlockName(Constants.MODID + ":blockFrameEnergy");
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
        return new TileFrameEnergy();
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
            TileFrameEnergy tile = (TileFrameEnergy)world.getTileEntity(x, y, z);
            if(tile != null) {
                player.openGui(ItemReplication.instance, GuiHandler.FRAME_ENERGY_GUI_ID, world, x, y, z);
            }
            return true;
        }
    }
}
