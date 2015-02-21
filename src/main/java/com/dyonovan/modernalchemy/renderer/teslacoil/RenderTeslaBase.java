package com.dyonovan.modernalchemy.renderer.teslacoil;

import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.model.teslacoil.ModelTeslaBase;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTeslaBase extends TileEntitySpecialRenderer {

    public static final ResourceLocation textureMain = new ResourceLocation(Constants.MODID + ":textures/models/teslaBase.png");
    public static final ResourceLocation textureSwitch = new ResourceLocation(Constants.MODID + ":textures/models/teslaBaseHandle.png");
    public static final ResourceLocation textureRotor = new ResourceLocation(Constants.MODID + ":textures/models/teslaBaseMag.png");

    private static float rotMod = 0.0F;

    private ModelTeslaBase model;

    public RenderTeslaBase() {
        this.model = new ModelTeslaBase();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
        TileTeslaBase base = (TileTeslaBase)tileentity;
        GL11.glPushMatrix();

        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        GL11.glDisable(GL11.GL_CULL_FACE);

        this.bindTexture(textureMain);
        this.model.renderMain();

        this.bindTexture(textureSwitch);
        this.model.renderSwitch();

        if(base.isCoilCharging()) {
            rotMod += 24.0F;
            GL11.glRotatef(rotMod, 0.0f, 1.0F, 0.0F);
        }
        this.bindTexture(textureRotor);
        this.model.renderRotor();
        GL11.glEnable(GL11.GL_CULL_FACE);

        GL11.glPopMatrix();
    }
}
