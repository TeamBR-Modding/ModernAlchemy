package com.dyonovan.itemreplication.blocks;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.handlers.GuiHandler;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.tileentity.TileSolidifier;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockSolidifier extends BlockBase {

    @SideOnly(Side.CLIENT)
    private IIcon front, frontActive;

    public BlockSolidifier() {
        super(Material.iron);
        this.setBlockName(Constants.MODID + ":blockSolidifier");
        this.setHardness(1.5F);
        this.setCreativeTab(ItemReplication.tabItemReplication);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {
        this.blockIcon = iconregister.registerIcon(Constants.MODID + ":solidifier_side");
        this.front = iconregister.registerIcon(Constants.MODID + ":solidifier_front_off");
        this.frontActive = iconregister.registerIcon(Constants.MODID + ":solidifier_front_on");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        boolean active = false;
        if (meta > 10) {
            meta -= 10;
            active = true;
        }
        return side == meta ? (active ? frontActive : front) : blockIcon;
    }

    public static boolean toggleIsActive(World world, int x, int y, int z) {
        int currentMeta = world.getBlockMetadata(x, y, z);
        if (currentMeta < 10) {
            world.setBlockMetadataWithNotify(x, y, z, currentMeta += 10, 2);
        } else {
            world.setBlockMetadataWithNotify(x, y, z, currentMeta -= 10, 2);
        }
        return !(currentMeta < 10);
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack) {
        int l = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0) {
            world.setBlockMetadataWithNotify(x, y, z, 2, 2);
        }

        if (l == 1) {
            world.setBlockMetadataWithNotify(x, y, z, 5, 2);
        }

        if (l == 2) {
            world.setBlockMetadataWithNotify(x, y, z, 3, 2);
        }

        if (l == 3) {
            world.setBlockMetadataWithNotify(x, y, z, 4, 2);
        }
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileSolidifier();
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
            TileSolidifier tile = (TileSolidifier)world.getTileEntity(x, y, z);
            if(tile != null) {
                player.openGui(ItemReplication.instance, GuiHandler.SOLIDIFIER_GUI_ID, world, x, y, z);
            }
            return true;
        }
    }
}
