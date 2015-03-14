package com.dyonovan.modernalchemy.blocks.arcfurnace;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.blocks.BlockBase;
import com.dyonovan.modernalchemy.blocks.BlockModernAlchemy;
import com.dyonovan.modernalchemy.blocks.IExpellable;
import com.dyonovan.modernalchemy.handlers.GuiHandler;
import com.dyonovan.modernalchemy.manual.component.ComponentBase;
import com.dyonovan.modernalchemy.manual.component.ComponentItemRender;
import com.dyonovan.modernalchemy.util.WorldUtils;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.TileArcFurnaceCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockArcFurnaceCore extends BlockModernAlchemy {
    @SideOnly(Side.CLIENT)
    private IIcon front;

    public BlockArcFurnaceCore() {
        super(Material.rock);
        this.setLightLevel(8F);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {
        setDefaultTexture(blockIcon = iconregister.registerIcon(Constants.MODID + ":blastFurnaceFront"));
    }
}
