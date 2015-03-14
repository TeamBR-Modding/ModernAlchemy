package com.dyonovan.modernalchemy.client.renderer.teslacoil;

import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.client.model.teslacoil.ModelTeslaStand;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTeslaStand extends TileEntitySpecialRenderer {

    public static final ResourceLocation texture = new ResourceLocation(Constants.MODID + ":textures/models/teslaStand.png");

    private ModelTeslaStand model;

    public RenderTeslaStand() {
        this.model = new ModelTeslaStand();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {

        GL11.glPushMatrix();

        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        GL11.glRotatef(180, 0F, 0F, 1F);

        this.bindTexture(texture);

        this.model.render();

        GL11.glPopMatrix();
    }
}
