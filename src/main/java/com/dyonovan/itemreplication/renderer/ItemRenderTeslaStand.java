package com.dyonovan.itemreplication.renderer;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemRenderTeslaStand implements IItemRenderer {

    private TileEntitySpecialRenderer render;
    private TileEntity tileentity;

    public ItemRenderTeslaStand(TileEntitySpecialRenderer render, TileEntity tileentity) {
        this.render = render;
        this.tileentity = tileentity;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        if(type != ItemRenderType.ENTITY && type != ItemRenderType.EQUIPPED) {

            GL11.glPushMatrix();
            this.render.renderTileEntityAt(this.tileentity, 0.0D, 0.0D, 0.0D, 0.0F);
            GL11.glPopMatrix();
        }
    }
}

