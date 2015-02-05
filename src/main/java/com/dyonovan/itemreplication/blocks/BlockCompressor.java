package com.dyonovan.itemreplication.blocks;

import com.dyonovan.itemreplication.TileEntities.TECompressor;
import com.dyonovan.itemreplication.handlers.BlockHandler;
import com.dyonovan.itemreplication.lib.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockCompressor extends BlockContainer {

    public boolean isRunning = false;

    @SideOnly(Side.CLIENT)
    private IIcon front, top;

    public BlockCompressor(boolean isRunning) {
        super(Material.anvil);
        this.setBlockName(Constants.MODID + ":compressor");
        this.setBlockUnbreakable();

        this.isRunning = isRunning;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {
        this.blockIcon = iconregister.registerIcon(Constants.MODID + ":compressor_side");
        this.front = iconregister.registerIcon(isRunning ? Constants.MODID + ":compressor_front_on" : Constants.MODID + ":compressor_front_off");
        this.top = iconregister.registerIcon(Constants.MODID + ":compressor_top");
    }

    public IIcon getIcon(int side, int meta) {
        if (side == 1) return this.top;
        else if (side == 0) return this.top;
        else if (meta == 2 && side == 2) return this.front;
        else if (meta == 3 && side == 5) return this.front;
        else if (meta == 0 && side == 3) return this.front;
        else if (meta == 1 && side == 4) return this.front;
        else return this.blockIcon;

    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack) {

        int whichDirectionFacing = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        world.setBlockMetadataWithNotify(x, y, z, whichDirectionFacing, 2);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }


    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TECompressor();
    }

    public static void updateFurnaceBlockState(boolean active, World world, int x, int y, int z)
    {
        int l = world.getBlockMetadata(x, y, z);
        TileEntity tileentity = world.getTileEntity(x, y, z);

        if (active)
        {
            world.setBlock(x, y, z, BlockHandler.blockCompressorOn);
        }
        else
        {
            world.setBlock(x, y, z, BlockHandler.blockCompressor);
        }

        world.setBlockMetadataWithNotify(x, y, z, l, 2);

        if (tileentity != null)
        {
            tileentity.validate();
            world.setTileEntity(x, y, z, tileentity);
        }
    }

}
