package com.dyonovan.modernalchemy.client.renderer.teslacoil;

import com.dyonovan.modernalchemy.client.model.teslacoil.ModelTeslaCoil;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTeslaCoil extends TileEntitySpecialRenderer {

    private ResourceLocation texture;

    private ModelTeslaCoil model;

    public RenderTeslaCoil(ResourceLocation resourceLocation) {
        this.model = new ModelTeslaCoil();
        texture = resourceLocation;
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {

        GL11.glPushMatrix();

        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        GL11.glRotatef(180, 0F, 1F, 0F);

        this.bindTexture(texture);

        this.model.render();

        GL11.glPopMatrix();
    }
}