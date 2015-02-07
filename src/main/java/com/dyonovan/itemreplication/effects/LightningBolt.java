package com.dyonovan.itemreplication.effects;

import com.dyonovan.itemreplication.lib.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class LightningBolt extends EntityFX {

    private ResourceLocation texture = new ResourceLocation(Constants.MODID + "textures/blocks/blastFurnace.png");

    public LightningBolt(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    public void renderParticle(Tessellator tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        super.renderParticle(tessellator, par2, par3, par4, par5, par6, par7);
        float x = (float)(this.posX - interpPosX);
        float y = (float)(this.posY - interpPosY);
        float z = (float)(this.posZ - interpPosZ);

        GL11.glDisable(GL11.GL_LIGHTING);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        float f2 = 0.5F;
        tessellator.setColorRGBA_F(0.9F * f2, 0.9F * f2, 1.0F * f2, 0.3F);

        tessellator.addVertexWithUV(x + 0.3, y, z + 0.3, 0.0, 0.0);
        tessellator.addVertexWithUV(x + 0.7, y, z + 0.3, 1.0, 0.0);
        tessellator.addVertexWithUV(x + 0.7, y, z + 0.7, 1.0, 1.0);
        tessellator.addVertexWithUV(x + 0.3, y, z + 0.7, 0.0, 1.0);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public void onUpdate()
    {

    }

}
