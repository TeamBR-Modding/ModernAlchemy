package com.dyonovan.modernalchemy.blocks.machines;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.blocks.BlockBase;
import com.dyonovan.modernalchemy.blocks.BlockModernAlchemy;
import com.dyonovan.modernalchemy.blocks.IExpellable;
import com.dyonovan.modernalchemy.handlers.GuiHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.manual.component.ComponentBase;
import com.dyonovan.modernalchemy.manual.component.ComponentItemRender;
import com.dyonovan.modernalchemy.proxy.ClientProxy;
import com.dyonovan.modernalchemy.tileentity.machines.TileAmalgamator;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.api.IIconProvider;
import openmods.block.BlockRotationMode;
import openmods.block.OpenBlock;

import java.util.ArrayList;
import java.util.List;

public class BlockAmalgamator extends BlockModernAlchemy {

    @SideOnly(Side.CLIENT)
    public static class Icons {
        public static IIcon front;
        public static IIcon side;
    }

    public BlockAmalgamator() {
        super(Material.iron);
        this.setHardness(1.5F);
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
        setRotationMode(BlockRotationMode.FOUR_DIRECTIONS);
        setPlacementMode(BlockPlacementMode.ENTITY_ANGLE);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {
        this.blockIcon = iconregister.registerIcon(Constants.MODID + ":amalgamator_side");
        Icons.front = iconregister.registerIcon(Constants.MODID + ":amalgamator_front");
        Icons.side = blockIcon;

        setTexture(ForgeDirection.NORTH, blockIcon);
        setTexture(ForgeDirection.SOUTH, Icons.front);
        setTexture(ForgeDirection.EAST, blockIcon);
        setTexture(ForgeDirection.WEST, blockIcon);
        setTexture(ForgeDirection.UP, blockIcon);
        setTexture(ForgeDirection.DOWN, blockIcon);
    }
}
