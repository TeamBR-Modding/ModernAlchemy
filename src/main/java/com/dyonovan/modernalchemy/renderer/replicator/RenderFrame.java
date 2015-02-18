package com.dyonovan.modernalchemy.renderer.replicator;

import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.model.replicator.ModelFrame;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderFrame extends TileEntitySpecialRenderer {

    public static final ResourceLocation texture = new ResourceLocation(Constants.MODID + ":textures/models/modelframe.png");

    private ModelFrame model;

    public RenderFrame() {
        this.model = new ModelFrame();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {

        GL11.glPushMatrix();

        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GL11.glRotatef(180, 0F, 0F, 1F);

        this.bindTexture(texture);

        //GL11.glPushMatrix();
        this.model.renderModel(0.0625F, tileentity.xCoord, tileentity.yCoord, tileentity.zCoord, (tileentity.getWorldObj() == null) ? true : false);
        //GL11.glPopMatrix();

        GL11.glPopMatrix();
    }
}
