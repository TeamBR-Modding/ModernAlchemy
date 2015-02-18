package com.dyonovan.modernalchemy.renderer.teslacoil;

import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.model.teslacoil.ModelTeslaBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTeslaBase extends TileEntitySpecialRenderer {

    public static final ResourceLocation texture = new ResourceLocation(Constants.MODID + ":textures/models/teslaBase.png");

    private ModelTeslaBase model;

    public RenderTeslaBase() {
        this.model = new ModelTeslaBase();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {

        GL11.glPushMatrix();

        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        GL11.glDisable(GL11.GL_CULL_FACE);
        this.bindTexture(texture);
        this.model.render();
        GL11.glEnable(GL11.GL_CULL_FACE);

        GL11.glPopMatrix();
    }
}
