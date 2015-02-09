package com.dyonovan.itemreplication.blocks.dummies;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.handlers.GuiHandler;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.tileentity.TileTeslaCoil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockOpenBelow extends Block {

    public BlockOpenBelow() {
        super(Material.cloth);
        this.setBlockName(Constants.MODID + ":blockGhost");
        this.setBlockBounds(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        TileEntity tile = world.getTileEntity(x, y - 1, z);
        if(tile != null) {
            if (world.isRemote)
            {
                return true;
            }
            else
            {
                if(tile instanceof TileTeslaCoil) {
                    player.openGui(ItemReplication.instance, GuiHandler.TESLA_COIL_GUI_ID, world, x, y - 1, z);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(Constants.MODID + ":null");
    }
}
