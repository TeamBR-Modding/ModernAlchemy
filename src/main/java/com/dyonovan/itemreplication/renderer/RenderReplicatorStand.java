package com.dyonovan.itemreplication.renderer;

import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.model.ModelReplicatorStand;
import com.dyonovan.itemreplication.tileentity.TileReplicatorStand;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderReplicatorStand extends TileEntitySpecialRenderer {

    public static final ResourceLocation texture = new ResourceLocation(Constants.MODID + ":textures/models/replicator_stand.png");

    private ModelReplicatorStand model;
    EntityItem entItem = null;

    public RenderReplicatorStand() {
        this.model = new ModelReplicatorStand();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {

        GL11.glPushMatrix();

        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GL11.glRotatef(180, 0F, 0F, 1F);

        this.bindTexture(texture);

        this.model.renderModel(0.0625F, tileentity.xCoord, tileentity.yCoord, tileentity.zCoord, (tileentity.getWorldObj() == null));

        GL11.glPopMatrix();

        //Render Item on Top
        int slot = 0;
        TileReplicatorStand tileEntity = (TileReplicatorStand)tileentity;

        if(tileEntity.getStackInSlot(slot) != null && (entItem == null || entItem.getEntityItem().getItem() != tileEntity.getStackInSlot(slot).getItem()))
            entItem = new EntityItem(tileEntity.getWorldObj(), x, y, z, tileEntity.getStackInSlot(slot));
        if (entItem != null) {
            GL11.glPushMatrix();
            this.entItem.hoverStart = 0.0F;
            RenderItem.renderInFrame = true;
            GL11.glTranslatef((float) x + 0.5F, (float) y + 1.02F, (float) z + 0.3F);
            GL11.glRotatef(180, 0, 1, 1);
            RenderManager.instance.renderEntityWithPosYaw(this.entItem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
            RenderItem.renderInFrame = false;
            GL11.glPopMatrix();
        }
    }

}
