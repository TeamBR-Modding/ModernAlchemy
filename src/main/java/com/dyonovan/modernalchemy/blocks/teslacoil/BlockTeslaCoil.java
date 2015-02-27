package com.dyonovan.modernalchemy.blocks.teslacoil;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.blocks.BlockBase;
import com.dyonovan.modernalchemy.handlers.GuiHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.manual.component.*;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockTeslaCoil extends BlockBase {

    public BlockTeslaCoil() {
        super(Material.iron);

        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
        this.setBlockName(Constants.MODID + ":blockTeslaCoil");
        this.setBlockBounds(0.34375F, 0F, 0.34375F, 0.65625F, 0.9F, 0.65625F);
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
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileTeslaCoil();
    }

    @Override
    public int getRenderType() {
        return -1;
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
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.getBlock(x, y - 1, z) instanceof BlockTeslaStand;
    }

    @Override
    public List<ComponentBase> getManualComponents() {
        List<ComponentBase> parts = new ArrayList<ComponentBase>();
        parts.add(new ComponentItemRender(30, new ItemStack(this)));
        parts.add(new ComponentTextBox("The Tesla coil is the main method of generating power." +
                "Set this block on top of a Tesla Stand and that on top of a Tesla Base to complete the structure."));
        parts.add(new ComponentHeader("Crafting"));
        parts.add(new ComponentCraftingRecipe(new ItemStack(this)));

        return parts;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);

        if (world.isRemote)
        {
            return true;
        } else {
            TileTeslaCoil tile = (TileTeslaCoil)world.getTileEntity(x, y, z);
            if(tile != null) {
                player.openGui(ModernAlchemy.instance, GuiHandler.TESLA_COIL_GUI_ID, world, x, y, z);
            }
            return true;
        }
    }
}
