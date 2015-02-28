package com.dyonovan.modernalchemy.blocks.arcfurnace.dummies;

import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.manual.component.ComponentBase;
import com.dyonovan.modernalchemy.manual.component.ComponentItemRender;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.dummies.TileDummyItemIO;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockItemIODummy extends BlockDummy {

    public BlockItemIODummy(String name) {
        super(name);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int par2) {
        return new TileDummyItemIO();
    }

    @Override
    public List<ComponentBase> getManualComponents() {
        List<ComponentBase> parts = new ArrayList<ComponentBase>();
        parts.add(new ComponentItemRender(30, new ItemStack(this)));
        return parts;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = register.registerIcon(Constants.MODID + ":blastFurnaceItemIO");
        active = register.registerIcon(Constants.MODID + ":blastFurnaceItemIOActive");
    }
}
