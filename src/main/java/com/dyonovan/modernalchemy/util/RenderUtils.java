package com.dyonovan.modernalchemy.util;

import com.dyonovan.modernalchemy.effects.LightningBolt;
import com.dyonovan.modernalchemy.handlers.PacketHandler;
import com.dyonovan.modernalchemy.network.RenderLightningBoltPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.awt.*;

public class RenderUtils {

    public static void renderCube(Tessellator tes, double x1, double y1, double z1, double x2, double y2, double z2) {
        tes.addVertex(x1, y1, z1);
        tes.addVertex(x1, y2, z1);
        tes.addVertex(x2, y2, z1);
        tes.addVertex(x2, y1, z1);

        tes.addVertex(x1, y1, z2);
        tes.addVertex(x2, y1, z2);
        tes.addVertex(x2, y2, z2);
        tes.addVertex(x1, y2, z2);

        tes.addVertex(x1, y1, z1);
        tes.addVertex(x1, y1, z2);
        tes.addVertex(x1, y2, z2);
        tes.addVertex(x1, y2, z1);

        tes.addVertex(x2, y1, z1);
        tes.addVertex(x2, y2, z1);
        tes.addVertex(x2, y2, z2);
        tes.addVertex(x2, y1, z2);

        tes.addVertex(x1, y1, z1);
        tes.addVertex(x2, y1, z1);
        tes.addVertex(x2, y1, z2);
        tes.addVertex(x1, y1, z2);

        tes.addVertex(x1, y2, z1);
        tes.addVertex(x1, y2, z2);
        tes.addVertex(x2, y2, z2);
        tes.addVertex(x2, y2, z1);
    }

    public static void renderCubeWithUV(Tessellator tes, double x1, double y1, double z1, double x2, double y2, double z2, double u, double v, double u1, double v1) {
        tes.addVertexWithUV(x1, y1, z1, u, v);
        tes.addVertexWithUV(x1, y2, z1, u, v1);
        tes.addVertexWithUV(x2, y2, z1, u1, v1);
        tes.addVertexWithUV(x2, y1, z1, u1, v);

        tes.addVertexWithUV(x1, y1, z2, u, v);
        tes.addVertexWithUV(x2, y1, z2, u, v1);
        tes.addVertexWithUV(x2, y2, z2, u1, v1);
        tes.addVertexWithUV(x1, y2, z2, u1, v);

        tes.addVertexWithUV(x1, y1, z1, u, v);
        tes.addVertexWithUV(x1, y1, z2, u, v1);
        tes.addVertexWithUV(x1, y2, z2, u1, v1);
        tes.addVertexWithUV(x1, y2, z1, u1, v);

        tes.addVertexWithUV(x2, y1, z1, u, v);
        tes.addVertexWithUV(x2, y2, z1, u, v1);
        tes.addVertexWithUV(x2, y2, z2, u1, v1);
        tes.addVertexWithUV(x2, y1, z2, u1, v);

        tes.addVertexWithUV(x1, y1, z1, u, v);
        tes.addVertexWithUV(x2, y1, z1, u, v1);
        tes.addVertexWithUV(x2, y1, z2, u1, v1);
        tes.addVertexWithUV(x1, y1, z2, u1, v);

        tes.addVertexWithUV(x1, y2, z1, u, v);
        tes.addVertexWithUV(x1, y2, z2, u, v1);
        tes.addVertexWithUV(x2, y2, z2, u1, v1);
        tes.addVertexWithUV(x2, y2, z1, u1, v);
    }

    @SideOnly(Side.CLIENT)
    public static void renderLightningBolt(World worldObj, double xCoord, double yCoord, double zCoord, TileEntity coil, int age) {
        renderLightningBolt(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, coil.xCoord + 0.5, coil.yCoord + 0.9, coil.zCoord + 0.5, age > 4 ? age : 4);
    }

    @SideOnly(Side.CLIENT)
    public static void renderLightningBolt(World worldObj, double xCoord, double yCoord, double zCoord, double tx, double ty, double tz, int age) {
        Minecraft.getMinecraft().effectRenderer.addEffect(new LightningBolt(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, tx, ty, tz, 1.6, 0.2, age > 4 ? age : 4, new Color(255, 255, 255, 255)));
    }

    @SideOnly(Side.CLIENT)
    public static void renderLightningBolt(World worldObj, double xCoord, double yCoord, double zCoord, double tx, double ty, double tz, double dis, double de, int age) {
        Minecraft.getMinecraft().effectRenderer.addEffect(new LightningBolt(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, tx, ty, tz, dis, de, age > 4 ? age : 4, new Color(255, 255, 255, 255)));
    }

    @SideOnly(Side.CLIENT)
    public static void renderLightningBolt(World worldObj, double xCoord, double yCoord, double zCoord, double tx, double ty, double tz, double dis, double de, int age, int r, int g, int b) {
        Minecraft.getMinecraft().effectRenderer.addEffect(new LightningBolt(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, tx, ty, tz, dis, de, age > 4 ? age : 4, new Color(r, g, b)));
    }

    public static void sendBoltToClient(int dimID, double x, double y, double z, double x1, double y1, double z1, double dis, double de, int age, int r, int g, int b) {
        PacketHandler.net.sendToDimension(new RenderLightningBoltPacket.BoltMessage(x, y, z, x1, y1, z1, dis, de, (age > 4 ? age : 4), r, g, b), dimID);
    }

    public static void sendBoltToClient(int dimID, double x, double y, double z, double x1, double y1, double z1, int age) {
        PacketHandler.net.sendToDimension(new RenderLightningBoltPacket.BoltMessage(x, y, z, x1, y1, z1, age > 4 ? age : 4), dimID);
    }

    public static void sendBoltToClient(double x, double y, double z, TileEntity coil, int age) {
        sendBoltToClient(coil.getWorldObj().provider.dimensionId, x, y, z, coil.xCoord + 0.5, coil.yCoord + 0.9, coil.zCoord + 0.5, age > 4 ? age : 4);
    }
}
