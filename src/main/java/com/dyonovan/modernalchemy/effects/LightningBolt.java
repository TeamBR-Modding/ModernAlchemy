package com.dyonovan.modernalchemy.effects;

import com.dyonovan.modernalchemy.lib.Constants;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Random;

public class LightningBolt extends EntityFX {

    private ResourceLocation texture = new ResourceLocation(Constants.MODID + ":textures/effects/lightning.png");

    private static Random rand = new Random();

    private float rotYaw = 0.0F;
    private float rotPitch = 0.0F;
    private double tX = 0.0D;
    private double tY = 0.0D;
    private double tZ = 0.0D;
    private double displacement = 1.6;
    private double detail = 0.2;
    private Color color;

    public LightningBolt(World world, double x, double y, double z, double targetX, double targetY, double targetZ, int age, Color c) {
        this(world, x, y, z, targetX, targetY, targetZ, 1.6, 0.2, age, c);
    }

    public LightningBolt(World world, double x, double y, double z, double targetX, double targetY, double targetZ, double dis, double de, int age, Color c) {
        super(world, x, y, z);
        color = c;
        this.particleMaxAge = age;
        this.particleAge = 0;
        this.tX = targetX;
        this.tY = targetY;
        this.tZ = targetZ;
        float xd = (float)(this.posX - this.tX);
        float yd = (float)(this.posY - this.tY);
        float zd = (float)(this.posZ - this.tZ);
        double var7 = MathHelper.sqrt_double(xd * xd + zd * zd);
        this.rotYaw = ((float)(Math.atan2(xd, zd) * 180.0D / 3.141592653589793D));
        this.rotPitch = ((float)(Math.atan2(yd, var7) * 180.0D / 3.141592653589793D));
        this.displacement = dis;
        this.detail = de;
    }

        @Override
    public void renderParticle(Tessellator tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        tessellator.draw();
        super.renderParticle(tessellator, par2, par3, par4, par5, par6, par7);
        renderBolt(tessellator, posX, posY, posZ, tX, tY, tZ, displacement, detail);
        Minecraft.getMinecraft().renderEngine.bindTexture((ResourceLocation) ReflectionHelper.getPrivateValue(EffectRenderer.class, null, new String[]{"particleTextures", "b", "field_110737_b"}));
        tessellator.startDrawingQuads();
    }

    private void renderBolt(Tessellator tessellator, double x1, double y1, double z1, double x2, double y2, double z2, double displacement, double detail) {
        if(displacement < detail) {
            float x = (float)(x1 - interpPosX);
            float y = (float)(y1 - interpPosY);
            float z = (float)(z1 - interpPosZ);

            float xd = (float)(x1 - x2);
            float yd = (float)(y1 - y2);
            float zd = (float)(z1 - z2);
            double var7 = MathHelper.sqrt_double(xd * xd + zd * zd);
            this.rotYaw = ((float)(Math.atan2(xd, zd) * 180.0D / 3.141592653589793D));
            this.rotPitch = ((float)(Math.atan2(yd, var7) * 180.0D / 3.141592653589793D));

            GL11.glPushMatrix();
            GL11.glTranslatef(x, y, z);

            GL11.glDepthMask(false);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 1);

            GL11.glDisable(2884);

            Minecraft.getMinecraft().renderEngine.bindTexture(texture);

            float ry = this.rotYaw;
            float rp = this.rotPitch;
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(180.0F + ry, 0.0F, 0.0F, -1.0F);
            GL11.glRotatef(rp, 1.0F, 0.0F, 0.0F);

            float size = 0.1F;
            double xx = size * -0.15;
            double xx2 = size * -0.15 * 1.0;
            double yy = MathHelper.sqrt_float(xd * xd + yd * yd + zd * zd);
            for(int i = 0; i < 3; i++) {
                GL11.glRotatef(60.0F, 0.0F, 1.0F, 0.0F);
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
                tessellator.addVertexWithUV(xx2, yy, 0.0, 0.0, 1.0);
                tessellator.addVertexWithUV(xx, 0, 0.0, 0.0, 0.0);
                tessellator.addVertexWithUV(-xx, 0, 0.0, 1.0, 0.0);
                tessellator.addVertexWithUV(-xx2, yy, 0.0, 1.0, 1.0);
                tessellator.draw();
            }
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(2884);
            GL11.glDisable(3042);
            GL11.glDepthMask(true);
            GL11.glPopMatrix();
        }
        else {
            double midX = (x2 + x1) / 2;
            double midY = (y2 + y1) / 2;
            double midZ = (z2 + z1) / 2;
            midX += (rand.nextFloat() - 0.5) * displacement;
            midY += (rand.nextFloat() - 0.5) * displacement;
            midZ += (rand.nextFloat() - 0.5) * displacement;
            renderBolt(tessellator, x1, y1, z1, midX, midY, midZ, displacement / 2, detail);
            renderBolt(tessellator, x2, y2, z2, midX, midY, midZ, displacement / 2, detail);
        }
    }

    @Override
    public void onUpdate()
    {
        particleAge++;
        if(particleAge > particleMaxAge)
            setDead();
    }

}
