package com.dyonovan.modernalchemy.renderer;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemRenderReplicatorStand implements IItemRenderer {

    private TileEntitySpecialRenderer render;
    private TileEntity tileentity;

    public ItemRenderReplicatorStand(TileEntitySpecialRenderer render, TileEntity tileentity) {
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

        switch (type) {
            case ENTITY: {
                GL11.glPushMatrix();
                this.render.renderTileEntityAt(this.tileentity, 0.0D, 0.0D, 0.0D, 0.0F);
                GL11.glPopMatrix();
            }
            case EQUIPPED: {
                GL11.glPushMatrix();
                this.render.renderTileEntityAt(this.tileentity, 0.0D, 0.0D, 0.0D, 0.0F);
                GL11.glPopMatrix();
            }
            case EQUIPPED_FIRST_PERSON: {
                GL11.glPushMatrix();
                this.render.renderTileEntityAt(this.tileentity, 0.0D, 0.0D, 0.0D, 0.0F);
                GL11.glPopMatrix();
            }
            case INVENTORY: {
                GL11.glPushMatrix();
                this.render.renderTileEntityAt(this.tileentity, 0.0D, 0.0D, 0.0D, 0.0F);
                GL11.glPopMatrix();
            }
            default:
                break;
        }
    }
}
