package com.dyonovan.itemreplication.renderer;

import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.model.ModelTeslaCoil;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTeslaCoil extends TileEntitySpecialRenderer {

    public static final ResourceLocation texture = new ResourceLocation(Constants.MODID + ":textures/models/teslaCoil.png");

    private ModelTeslaCoil model;

    public RenderTeslaCoil() {
        this.model = new ModelTeslaCoil();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {

        GL11.glPushMatrix();

        GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
        GL11.glRotatef(180, 0F, 1F, 0F);

        this.bindTexture(texture);

        this.model.render();
        //this.model.renderModel(0.0625F, tileentity.xCoord, tileentity.yCoord, tileentity.zCoord, (tileentity.getWorldObj() == null));

        GL11.glPopMatrix();
    }
}