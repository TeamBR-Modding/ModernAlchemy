package com.dyonovan.modernalchemy.blocks.machines;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.blocks.BlockBase;
import com.dyonovan.modernalchemy.blocks.IExpellable;
import com.dyonovan.modernalchemy.handlers.GuiHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.manual.component.ComponentBase;
import com.dyonovan.modernalchemy.manual.component.ComponentItemRender;
import com.dyonovan.modernalchemy.proxy.ClientProxy;
import com.dyonovan.modernalchemy.tileentity.BaseTile;
import com.dyonovan.modernalchemy.tileentity.machines.TileAdvancedCrafter;
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
import net.minecraftforge.common.util.ForgeDirection;
import openmods.block.BlockRotationMode;
import openmods.block.OpenBlock;

import java.util.ArrayList;
import java.util.List;

public class BlockAdvancedCrafter extends OpenBlock {

    @SideOnly(Side.CLIENT)
    private static class Icons {
        public static IIcon front, side;
    }

    public BlockAdvancedCrafter() {
        super(Material.iron);
        this.setHardness(1.5F);
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
        setRotationMode(BlockRotationMode.FOUR_DIRECTIONS);
    }

    @Override
    public int getRenderType() {
        return ClientProxy.renderId;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {
        Icons.side = iconregister.registerIcon(Constants.MODID + ":crafter_side");
        Icons.front = iconregister.registerIcon(Constants.MODID + ":crafter_front");

        setTexture(ForgeDirection.NORTH, Icons.side);
        setTexture(ForgeDirection.SOUTH, Icons.front);
        setTexture(ForgeDirection.EAST, Icons.side);
        setTexture(ForgeDirection.WEST, Icons.side);
        setTexture(ForgeDirection.UP, Icons.side);
        setTexture(ForgeDirection.DOWN, Icons.side);
    }

    @Override
    protected Object getModInstance() {
        return ModernAlchemy.instance;
    }
}
